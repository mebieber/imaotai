package com.oddfar.campus.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AESUtil {

    private static final String AES_KEY = "76ede6972caa4ba9";

    public static void main(String[] args) throws Exception {
        String testData = "{\"parkId\":\"10162606\",\"mobileNumber\":\"18774268657\",\"visitDate\":\"2024-05-02\",\"uid\":\"413541235741235947\",\"carNumber\":\"蒙F99999\"}";
        String encrypted = encryptData(testData);
        System.out.println("Encrypted: " + encrypted);

        String responseData = "xmAl64rQ7YDIomF7HKCJRek/Z4bJQv+2dL34ZArk261XVOE+0nsjYFR7l0TXcRem25xZg8zunykRU2+Rh0gxRO/J88CSBtqTPBufHokRnKPVgzNCXfbsMk9NfeVHhor/OCUTVAHYyjXqQTR2eeZknQ==";
        String decrypted = decryptData(responseData);
        System.out.println("Decrypted: " + decrypted);
    }

    public static String encryptData(String data) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(encryptedBytes);
    }

    public static String decryptData(String encryptedData) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] decodedBytes = Base64.decodeBase64(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
