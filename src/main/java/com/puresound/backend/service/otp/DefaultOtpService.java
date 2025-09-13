package com.puresound.backend.service.otp;

import com.puresound.backend.dto.otp.VerifyOtpEmailRequest;
import com.puresound.backend.util.OtpUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class DefaultOtpService implements OtpService {
    final RedisTemplate<String, Object> redisTemplate;
    static final String COMMON_OTP_PREFIX = "otp:common:";
    @Value("${otp.signup.exp-min}")
    long signupExp;

    @Override
    public String generateCommonOtp(String email) {
        String key = COMMON_OTP_PREFIX + email;
        String otp = OtpUtil.generateOtp(6);
        redisTemplate.opsForValue().set(key, otp, Duration.ofMinutes(signupExp));
        return otp;
    }

    @Override
    public boolean verifyCommonOtp(VerifyOtpEmailRequest request) {
        String key = COMMON_OTP_PREFIX + request.email();
        String storedOtp = (String) redisTemplate.opsForValue().get(key);
        if (storedOtp != null && storedOtp.equals(request.otp())) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }
}
