package com.udacity.jwdnd.course1.cloudstorage.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class HashService {

    private static final Logger logger = LoggerFactory.getLogger(HashService.class);

    public String getHashedValue(String data, String salt) {
        byte[] hashedValue = null;

        int iterCount = 10000; 
        int derivedKeyLength = 256; 
        KeySpec spec = new PBEKeySpec(data.toCharArray(), salt.getBytes(), iterCount, derivedKeyLength);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            hashedValue = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            logger.error("Error occurred while hashing the value: " + e.getMessage());
        }

        if (hashedValue != null) {
            return Base64.getEncoder().encodeToString(hashedValue);
        } else {
            return null; // Trả về null nếu không thể băm dữ liệu
        }
    }
}
