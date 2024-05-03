package com.oddfar.campus.business.third.wts.request;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 车辆预约提交参数
 *
 * @author chenjiahui
 * @date 2024/5/3 9:17
 */
@Data
public class WtsCarAppointmentSubmitRequest extends WtsBaseRequest {

    private static final long serialVersionUID = -1885350801976398807L;

    /**
     * 车牌号
     */
    private String appointmentVehicle;
    /**
     * 访客姓名
     */
    private String visitorName;
    /**
     * 手机号码
     */
    private String mobileNumber;
    /**
     * 接待人（可为空）
     */
    private String receptionist;
    /**
     * 预约日期
     * 案例：1714665600 预约日期的时间戳
     */
    private String appointmentDate;
    /**
     * 预约原因（可为空）
     */
    private String appointmentReason;
    /**
     * 手机短信验证码
     */
    private String code;
    /**
     * 预约时间（2024-05-03 00:00:00）
     */
    private String timeff;
    /**
     * 车场id（10162606）
     */
    private String parkId;
    /**
     * 身份证号码
     */
    private String uid;
    /**
     * 类型（1）
     */
    private Integer type;

    public Map<String, String> toMap() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appointmentVehicle", appointmentVehicle);
        paramMap.put("visitorName", visitorName);
        paramMap.put("mobileNumber", mobileNumber);
        paramMap.put("receptionist", receptionist);
        paramMap.put("appointmentDate", appointmentDate);
        paramMap.put("appointmentReason", appointmentReason);
        paramMap.put("code", code);
        paramMap.put("timeff", timeff);
        paramMap.put("parkId", parkId);
        paramMap.put("uid", uid);
        paramMap.put("type", type.toString());
        return paramMap;
    }

}
