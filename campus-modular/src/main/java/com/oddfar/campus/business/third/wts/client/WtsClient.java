package com.oddfar.campus.business.third.wts.client;

import cn.hutool.http.*;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.oddfar.campus.business.third.wts.request.WtsCarAppointmentSubmitRequest;
import com.oddfar.campus.business.third.wts.request.WtsSendSmsRequest;
import com.oddfar.campus.business.third.wts.response.WtsBaseResponse;
import com.oddfar.campus.common.exception.ServiceException;
import com.oddfar.campus.common.utils.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 车辆预约发送请求
 *
 * @author chenjiahui
 * @date 2024/5/3 9:39
 */
public class WtsClient {

    private static final Logger logger = LoggerFactory.getLogger(WtsClient.class);

    public static Boolean appointmentSendCode(WtsSendSmsRequest param) {
        HttpRequest request = HttpUtil.createRequest(Method.POST,
                "https://fapi.appykt.com/parking-system/visitor/applyCode");

        request.header("User-Agent", "iOS;16.3;Apple;?unrecognized?");
        request.header("Content-Type", "application/json");
        request.header("Park-Mobile-Auth", param.getToken());

        String apiParam = param.signature();
        HttpResponse execute = request.body(apiParam).execute();
        WtsBaseResponse response = parseSendCodeResult(execute.body());

        logger.info("「车辆预约发送验证码返回」：" + response);
        if (!response.getCode().equals(HttpStatus.HTTP_OK)) {
            logger.error("「车辆预约发送验证码失败」：apiParam:{}, response:{}", apiParam, response);
            throw new ServiceException("发送验证码失败：" + response.getMsg());
        }
        return Boolean.TRUE;
    }

    public static Boolean appointmentSubmit(WtsCarAppointmentSubmitRequest param) {
        HttpRequest request = HttpUtil.createRequest(Method.POST,
                "https://fapi.appykt.com/parking-system/visitor/apply");

        request.header("User-Agent", "iOS;16.3;Apple;?unrecognized?");
        request.header("Content-Type", "application/x-www-form-urlencoded");
        request.header("Park-Mobile-Auth", param.getToken());

        HttpResponse execute = request.formStr(param.toMap()).execute();
        WtsBaseResponse response = JSON.parseObject(execute.body(), WtsBaseResponse.class);

        logger.info("「车辆预约提交返回」：" + response);
        if (!response.getCode().equals(HttpStatus.HTTP_OK)) {
            logger.error("「车辆预约提交失败」：apiParam:{}, response:{}", param, execute.body());
            throw new ServiceException("车辆预约提交失败：" + response.getMsg());
        }
        return Boolean.TRUE;
    }

    private static WtsBaseResponse parseSendCodeResult(String body) {
        try {
            return JSONObject.parseObject(AESUtil.decryptData(body), WtsBaseResponse.class);
        } catch (Exception e) {
            logger.error("「车辆预约发送验证码结果解析失败」body:{}", body, e);
            throw new ServiceException("车辆预约发送验证码结果解析失败");
        }
    }
}
