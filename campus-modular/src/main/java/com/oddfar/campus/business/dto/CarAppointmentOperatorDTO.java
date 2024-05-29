package com.oddfar.campus.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 车辆预约操作入参
 *
 * @author chenjiahui
 * @date 2024/5/2 15:11
 */
@Data
public class CarAppointmentOperatorDTO implements Serializable {

    private static final long serialVersionUID = -9144037662263508240L;

    @NotBlank(message = "车牌号不能为空")
    @ApiModelProperty("车牌号")
    private String carNumber;

    @NotBlank(message = "手机号码不能为空")
    @ApiModelProperty("手机号码")
    private String mobileNumber;

    @NotBlank(message = "驾驶员姓名不能为空")
    @ApiModelProperty("驾驶员姓名")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "预约时间不能为空")
    @ApiModelProperty("预约时间")
    private Date visitDate;

    @NotBlank(message = "uid不能为空")
    @ApiModelProperty("身份证号码")
    private String uid;

    @NotBlank(message = "token不能为空")
    @ApiModelProperty("TOKEN")
    private String token;

}
