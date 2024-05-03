package com.oddfar.campus.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 车辆预约实体表
 *
 * @author chenjiahui
 * @date 2024/5/2 14:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("i_car_appointment")
public class CarAppointment extends BaseEntity {

    private static final long serialVersionUID = -841274601684980850L;

    @ApiModelProperty("预约编码")
    private String appointmentCode;

    @ApiModelProperty("车牌号")
    private String carNumber;

    @ApiModelProperty("手机号码")
    private String mobileNumber;

    @ApiModelProperty("驾驶员姓名")
    private String name;

    @ApiModelProperty("预约时间")
    private Date visitDate;

    @ApiModelProperty("车场ID")
    private String parkId;

    @ApiModelProperty("身份证号码")
    private String uid;

    @ApiModelProperty("TOKEN")
    private String token;

}
