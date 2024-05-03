package com.oddfar.campus.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LicensePlateValidator {

    public static final String[] patterns = {
            "^[京津沪渝冀豫云辽黑湘皖鲁赣鄂桂甘陕晋内蒙古苏浙闽赣鲁豫鄂湘粤桂琼川贵云藏陕甘青宁新][A-Z]{1}[A-Z0-9]{5}$", // 普通车牌
            "^[京津沪渝冀豫云辽黑湘皖鲁赣鄂桂甘陕晋内蒙古苏浙闽赣鲁豫鄂湘粤桂琼川贵云藏陕甘青宁新][A-Z]{1}[A-Z0-9]{5}[DF]$" // 新能源车牌
    };

    public static void main(String[] args) {
        String input = "粤B99999";

        for (String patternStr : patterns) {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                System.out.println("车牌号格式正确：" + input);
                break;
            }
        }
    }

    /**
     * 校验车牌号是否符合常见格式。
     *
     * @param licensePlate 要校验的车牌号
     * @return 如果格式正确返回true，否则返回false
     */
    public static boolean validateLicensePlate(String licensePlate) {
        // 遍历每个正则表达式进行匹配
        for (String patternStr : patterns) {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(licensePlate);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }
}
