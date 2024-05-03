package com.oddfar.campus.business.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 车辆预约提交入参
 *
 * @author chenjiahui
 * @date 2024/5/2 15:11
 */
@Data
public class CarAppointmentSubmitDTO implements Serializable {

    private static final long serialVersionUID = -8541897658704343574L;

    @NotNull
    private Long id;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty("验证码")
    private String smsCode;

}
