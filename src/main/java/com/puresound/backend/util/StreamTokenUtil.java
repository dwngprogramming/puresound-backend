package com.puresound.backend.util;

import lombok.experimental.UtilityClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@UtilityClass
public class StreamTokenUtil {
    public static String generateStreamingVerifyToken(String trackId, Integer bitrate, long exp, String secretKey) {
        validateParameters(trackId, bitrate, exp, secretKey);

        String data = trackId + ":" + bitrate + ":" + exp;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String hmacHex = HexFormat.of().formatHex(hmacBytes);
            return String.format("token_=exp=%d~hmac=%s", exp, hmacHex);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC", e);
        }
    }

    private static void validateParameters(String trackId, Integer bitrate, long exp, String secretKey) {
        if (trackId == null || trackId.isBlank()) {
            throw new IllegalArgumentException("TrackId must not be null or empty");
        }
        if (bitrate == null || bitrate <= 0) {
            throw new IllegalArgumentException("Bitrate must not be null and must be greater than zero");
        }
        if (exp <= 0) {
            throw new IllegalArgumentException("Expiration time must be positive");
        }
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException("Secret key must not be null or empty");
        }
    }
}
