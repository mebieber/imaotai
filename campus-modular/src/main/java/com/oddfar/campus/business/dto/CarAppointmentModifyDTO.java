package com.oddfar.campus.business.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 车辆预约修改入参
 *
 * @author chenjiahui
 * @date 2024/5/2 15:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CarAppointmentModifyDTO extends CarAppointmentOperatorDTO {

    private static final long serialVersionUID = -6358723796957664697L;

    @NotNull
    private Long id;

}
