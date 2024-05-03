package com.oddfar.campus.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 车辆预约日志实体表
 *
 * @author chenjiahui
 * @date 2024/5/2 14:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("i_car_appointment_log")
public class CarAppointmentLog extends BaseEntity {

    private static final long serialVersionUID = -6511677699868018184L;

    @ApiModelProperty("车辆预约表ID")
    private Long carAppointmentId;

    @ApiModelProperty("日志内容")
    private String content;

}
