package com.nahid.main.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class HashUtil {

    private final String secretKey;

    public HashUtil(@Value("${encryption.password}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String hash(String data) {
        if (data == null) return null;
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            hmac.init(keySpec);

            byte[] hash = hmac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }
}
