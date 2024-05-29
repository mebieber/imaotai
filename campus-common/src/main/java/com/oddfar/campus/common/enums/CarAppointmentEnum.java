package com.oddfar.campus.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 车辆预约状态枚举
 *
 * @author chenjiahui
 * @date 2024/5/3 17:05
 */
@Getter
@AllArgsConstructor
public enum CarAppointmentEnum {

    /**
     * 业务类
     */
    FAILED(-1, "预约失败"),
    WAIT(0, "待处理"),
    SUCCESS(1, "预约成功"),
    SEND_SMS_SUCCESS(2, "短信发送成功");

    final Integer code;
    final String message;
}
