package com.puresound.backend.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class OtpUtil {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateOtp(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }

        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(SECURE_RANDOM.nextInt(10)); // 0-9
        }
        return otp.toString();
    }
}
