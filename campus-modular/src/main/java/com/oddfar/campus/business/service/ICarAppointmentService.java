package com.oddfar.campus.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oddfar.campus.business.dto.CarAppointmentCreateDTO;
import com.oddfar.campus.business.dto.CarAppointmentModifyDTO;
import com.oddfar.campus.business.dto.CarAppointmentSubmitDTO;
import com.oddfar.campus.business.entity.CarAppointment;
import com.oddfar.campus.business.entity.IShop;

public interface ICarAppointmentService extends IService<CarAppointment> {


    Boolean create(CarAppointmentCreateDTO createDTO);

    Boolean modify(CarAppointmentModifyDTO modifyDTO);

    void sendCode(Long id);

    void submit(CarAppointmentSubmitDTO dto);
}
