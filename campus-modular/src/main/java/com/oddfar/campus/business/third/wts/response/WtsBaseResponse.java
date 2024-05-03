package com.oddfar.campus.business.third.wts.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 公共响应参数
 *
 * @author chenjiahui
 * @date 2024/5/3 9:35
 */
@Data
public class WtsBaseResponse implements Serializable {

    private static final long serialVersionUID = -3318279401453168857L;

    private Integer code;

    private String msg;

    private Long time;
}
