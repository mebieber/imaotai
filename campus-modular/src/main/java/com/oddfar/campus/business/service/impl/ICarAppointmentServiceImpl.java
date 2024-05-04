package com.oddfar.campus.business.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.PhoneUtil;
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
import com.oddfar.campus.common.enums.CarAppointmentEnum;
import com.oddfar.campus.common.utils.JWTUtil;
import com.oddfar.campus.common.utils.LicensePlateValidator;
import com.oddfar.campus.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ICarAppointmentServiceImpl extends ServiceImpl<CarAppointmentMapper, CarAppointment> implements ICarAppointmentService {

    private final ICarAppointmentLogService carAppointmentLogService;

    public static final String PARK_ID = "10162606";

    @Override
    public Boolean create(CarAppointmentCreateDTO createDTO) {
        checkOperatorParam(createDTO);
        CarAppointment entity = new CarAppointment();
        BeanUtils.copyProperties(createDTO, entity);
        entity.setAppointmentCode(generateAppointmentCode());
        entity.setStatus(0);
        entity.setParkId(PARK_ID);
        entity.setCreateUser(SecurityUtils.getUserId());
        entity.setCreateTime(new Date());
        return save(entity);
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

}
