package com.oddfar.campus.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oddfar.campus.business.entity.CarAppointmentLog;

public interface ICarAppointmentLogService extends IService<CarAppointmentLog> {

    void saveLog(CarAppointmentLog log);
}
