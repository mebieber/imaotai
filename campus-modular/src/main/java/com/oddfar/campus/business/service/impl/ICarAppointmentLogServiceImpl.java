package com.oddfar.campus.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oddfar.campus.business.entity.CarAppointmentLog;
import com.oddfar.campus.business.mapper.CarAppointmentLogMapper;
import com.oddfar.campus.business.service.ICarAppointmentLogService;
import com.oddfar.campus.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class ICarAppointmentLogServiceImpl extends ServiceImpl<CarAppointmentLogMapper, CarAppointmentLog> implements ICarAppointmentLogService {

    @Override
    public void saveLog(CarAppointmentLog log) {
        log.setCreateUser(SecurityUtils.getUserId());
        log.setCreateTime(new Date());
        save(log);
    }

}
