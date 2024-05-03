package com.oddfar.campus.business.third.wts.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 公共请求参数
 *
 * @author chenjiahui
 * @date 2024/5/3 10:43
 */
@Data
public class WtsBaseRequest implements Serializable {

    private static final long serialVersionUID = 4965755913412812319L;

    private String token;
}
