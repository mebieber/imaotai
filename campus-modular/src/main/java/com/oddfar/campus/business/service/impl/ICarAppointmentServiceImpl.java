package com.oddfar.campus.business.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.PhoneUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oddfar.campus.business.dto.CarAppointmentCreateDTO;
import com.oddfar.campus.business.dto.CarAppointmentModifyDTO;
import com.oddfar.campus.business.dto.CarAppointmentOperatorDTO;
import com.oddfar.campus.business.dto.CarAppointmentSubmitDTO;
import com.oddfar.campus.business.entity.CarAppointment;
import com.oddfar.campus.business.entity.CarAppointmentLog;
import com.oddfar.campus.business.mapper.CarAppointmentMapper;
import com.oddfar.campus.business.service.ICarAppointmentLogService;
import com.oddfar.campus.business.service.ICarAppointmentService;
import com.oddfar.campus.business.third.wts.client.WtsClient;
import com.oddfar.campus.business.third.wts.request.WtsCarAppointmentSubmitRequest;
import com.oddfar.campus.business.third.wts.request.WtsSendSmsRequest;
import com.oddfar.campus.common.core.RedisCache;
import com.oddfar.campus.common.enums.CarAppointmentEnum;
import com.oddfar.campus.common.utils.JWTUtil;
import com.oddfar.campus.common.utils.LicensePlateValidator;
import com.oddfar.campus.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ICarAppointmentServiceImpl extends ServiceImpl<CarAppointmentMapper, CarAppointment> implements ICarAppointmentService {

    private final ICarAppointmentLogService carAppointmentLogService;

    private final RedisCache redisCache;

    public static final String PARK_ID = "10162606";
    public static final int MAX_COUNT = 10;
    public static final long MAX_AGING = 2 * 60 * 1000;
    public static final String CAR_KEY = "car:key:";

    @Override
    public Long create(CarAppointmentCreateDTO createDTO) {
        checkOperatorParam(createDTO);
        CarAppointment entity = new CarAppointment();
        BeanUtils.copyProperties(createDTO, entity);
        entity.setAppointmentCode(generateAppointmentCode());
        entity.setStatus(0);
        entity.setParkId(PARK_ID);
        entity.setCreateUser(SecurityUtils.getUserId());
        entity.setCreateTime(new Date());
        save(entity);
        return entity.getId();
    }

    @Override
    public Boolean modify(CarAppointmentModifyDTO modifyDTO) {
        checkOperatorParam(modifyDTO);
        CarAppointment entity = new CarAppointment();
        BeanUtils.copyProperties(modifyDTO, entity);
        entity.setUpdateUser(SecurityUtils.getUserId());
        entity.setUpdateTime(new Date());
        return updateById(entity);
    }

    private void checkToken(String token) {
        String payload = JWTUtil.parsePayload(token);
        Assert.notNull(payload, "token解析失败");
        JSONObject jsonObject = JSONObject.parseObject(payload);
        Long expTime = jsonObject.getLong("exp");
        Assert.notNull(expTime, "无效token");
        Assert.isTrue(DateUtil.current() < expTime * 1000, "token已过期");
    }

    private void checkOperatorParam(CarAppointmentOperatorDTO dto) {
        checkToken(dto.getToken());
        Assert.isTrue(IdcardUtil.isValidCard(dto.getUid()), "身份证号码格式有误!");
        Assert.isTrue(PhoneUtil.isMobile(dto.getMobileNumber()), "手机号格式有误!");
        Assert.isTrue(LicensePlateValidator.validateLicensePlate(dto.getCarNumber()), "车牌号格式有误!");
    }

    @Override
    public void sendCode(Long id) {
        CarAppointment carAppointment = getById(id);
        Assert.notNull(carAppointment, "车辆预约信息查找为空");

        CarAppointmentLog log = new CarAppointmentLog();
        log.setCarAppointmentId(carAppointment.getId());

        WtsSendSmsRequest request = new WtsSendSmsRequest();
        BeanUtils.copyProperties(carAppointment, request);
        request.setVisitDate(DateUtil.format(carAppointment.getVisitDate(), DatePattern.NORM_DATE_FORMAT));

        try {
            Boolean result = WtsClient.appointmentSendCode(request);
            Assert.isTrue(Objects.equals(result, Boolean.TRUE), "验证码发送失败");
            carAppointment.setStatus(CarAppointmentEnum.SEND_SMS_SUCCESS.getCode());
            log.setContent("发送短信成功");
        } catch (Exception e) {
            log.setContent("失败原因：" + e.getMessage());
            carAppointment.setStatus(CarAppointmentEnum.FAILED.getCode());
            updateById(carAppointment);
            throw e;
        } finally {
            carAppointmentLogService.saveLog(log);
        }
    }

    @Override
    public void submit(CarAppointmentSubmitDTO dto) {
        CarAppointment carAppointment = getById(dto.getId());
        Assert.notNull(carAppointment, "车辆预约信息查找为空");

        CarAppointmentLog log = new CarAppointmentLog();
        log.setCarAppointmentId(carAppointment.getId());

        try {
            WtsCarAppointmentSubmitRequest request = buildSubmitRequest(carAppointment, dto.getSmsCode());
            Boolean result = WtsClient.appointmentSubmit(request);
            Assert.isTrue(Objects.equals(result, Boolean.TRUE), "车辆预约失败");
            log.setContent("预约成功");
            carAppointment.setStatus(CarAppointmentEnum.SUCCESS.getCode());
        } catch (Exception e) {
            carAppointment.setStatus(CarAppointmentEnum.FAILED.getCode());
            log.setContent("失败原因：" + e.getMessage());
            throw e;
        } finally {
            carAppointmentLogService.saveLog(log);
            updateById(carAppointment);
        }
    }

    public WtsCarAppointmentSubmitRequest buildSubmitRequest(CarAppointment carAppointment, String smsCode) {
        WtsCarAppointmentSubmitRequest request = new WtsCarAppointmentSubmitRequest();
        request.setAppointmentVehicle(carAppointment.getCarNumber());
        request.setVisitorName(carAppointment.getName());
        request.setMobileNumber(carAppointment.getMobileNumber());
        request.setAppointmentDate(convertTime(carAppointment.getVisitDate()));
        request.setCode(smsCode);
        request.setTimeff(DateUtil.format(carAppointment.getVisitDate(), DatePattern.NORM_DATETIME_FORMAT));
        request.setParkId(carAppointment.getParkId());
        request.setUid(carAppointment.getUid());
        request.setType(1);
        request.setToken(carAppointment.getToken());
        return request;
    }

    private String convertTime(Date date) {
        Assert.notNull(date, "date.is.null");
        return String.valueOf(date.getTime() / 1000);
    }

    private String generateAppointmentCode() {
        return "CA" + System.nanoTime();
    }

    public void findDetail(Long id) {

    }

    public void findPage() {

    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void autoSendSmsCodeTask() {
        List<CarAppointment> waitList = getValidList();
        log.info("auto send sms code task start. list:{}", JSON.toJSONString(waitList));

        for (CarAppointment carAppointment : waitList) {
            Long id = carAppointment.getId();
            try {
                if (isValid(id)) {
                    sendCode(id);
                }
            } catch (Exception e) {
                log.error("auto send sms code task failed. id:{}, cause:{}", id, e.getMessage());
            }
        }
    }

    private List<CarAppointment> getValidList() {
        Date startDate = DateUtil.beginOfDay(new Date());
        return lambdaQuery()
                .select(CarAppointment::getId)
                .in(CarAppointment::getStatus, CarAppointmentEnum.WAIT.getCode(), CarAppointmentEnum.FAILED.getCode())
                .ge(CarAppointment::getVisitDate, startDate)
                .list();
    }

    private boolean isValid(Long id) {
        Long count = addCache(id);
        if (count > MAX_COUNT) {
            log.info("时效内抢票次数超过最大限制[{}]", id);
            return false;
        }
        return true;
    }

    private Long addCache(Long id) {
        // 注意多线程情况下要加锁
        String key = CAR_KEY + id;
        boolean hasKey = redisCache.hasKey(key);
        Long count = redisCache.increment(key);
        if (!hasKey) {
            redisCache.expire(key, MAX_AGING, TimeUnit.MILLISECONDS);
        }
        return count;
    }

}
