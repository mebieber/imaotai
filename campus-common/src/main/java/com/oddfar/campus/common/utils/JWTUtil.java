package com.oddfar.campus.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * JWT工具类
 *
 * @author chenjiahui
 * @date 2024/5/3 10:46
 */
public class JWTUtil {

    public static void main(String[] args) {
        String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE3MTQ2NzExMTgsIm9wZW5JZCI6Im9WSFhDMWU4bnloUlRHMEVLeEFIbzdCYW9HVkUiLCJhcHBJZCI6Ind4MTQ3MTZhOTBhMTE3NTkwNyIsImV4cCI6MTcxNjMxMTM4MH0.fO5n226XsjxP7hbP3uhKffu8qTRsWnQrjwqFNZzbsmk";

        // 解码 JWT 的前两部分
        String[] jwtParts = jwt.split("\\.");
        if (jwtParts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }

        byte[] decodedHeader = Base64.getUrlDecoder().decode(jwtParts[0]);
        byte[] decodedPayload = Base64.getUrlDecoder().decode(jwtParts[1]);

        // 将解码后的字节转换回 JSON 对象
        String headerJson = new String(decodedHeader, StandardCharsets.UTF_8);
        String payloadJson = new String(decodedPayload, StandardCharsets.UTF_8);

        System.out.println("Header: " + headerJson);
        System.out.println("Payload: " + payloadJson);
    }

    public static String parsePayload(String jwt) {
        // 解码 JWT 的前两部分
        String[] jwtParts = jwt.split("\\.");
        if (jwtParts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }
        byte[] decodedPayload = Base64.getUrlDecoder().decode(jwtParts[1]);
        return new String(decodedPayload, StandardCharsets.UTF_8);
    }

}
