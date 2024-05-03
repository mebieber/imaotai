package com.oddfar.campus.business.third.wts.request;

import com.alibaba.fastjson2.JSON;
import com.oddfar.campus.common.exception.ServiceException;
import com.oddfar.campus.common.utils.AESUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 车辆预约发送短信参数
 *
 * @author chenjiahui
 * @date 2024/5/3 9:17
 */
@Data
public class WtsSendSmsRequest extends WtsBaseRequest {

    private static final long serialVersionUID = 8467655385252273589L;

    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 手机号码
     */
    private String mobileNumber;
    /**
     * 预约时间（2024-05-03）
     */
    private String visitDate;
    /**
     * 车场id（10162606）
     */
    private String parkId;
    /**
     * 身份证号码
     */
    private String uid;

    public String signature() {
        try {
            String payload = JSON.toJSONString(this);
            return AESUtil.encryptData(payload);
        } catch (Exception e) {
            throw new ServiceException("send.sms.param.sign.failed");
        }
    }

}
