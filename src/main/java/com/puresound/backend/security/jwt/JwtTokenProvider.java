package com.puresound.backend.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    String secretKey;

    @Value("${jwt.exp-at-min}")
    long expAtMin;

    @Value("${jwt.exp-rt-min}")
    long expRtMin;

    final JwtDecoder jwtDecoder;

    /**
     * Generate JWT access token
     */
    public String generateAccessToken(UserPrincipal userPrincipal) {
        try {
            Instant now = Instant.now();
            Instant expiry = now.plus(expAtMin, ChronoUnit.MINUTES);

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(userPrincipal.id())
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expiry))
                    .claim("fullname", userPrincipal.fullname())
                    .claim("userType", userPrincipal.userType().name())
                    .claim("authorities", new ArrayList<>(userPrincipal.authorities()))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claims
            );

            JWSSigner signer = new MACSigner(secretKey.getBytes(StandardCharsets.UTF_8));
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    /**
     * Generate JWT access token
     */
    public String generateRefreshToken(UserPrincipal userPrincipal) {
        try {
            Instant now = Instant.now();
            Instant expiry = now.plus(expRtMin, ChronoUnit.MINUTES);

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(userPrincipal.id())
                    .claim("userType", userPrincipal.userType().name())
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expiry))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claims
            );

            JWSSigner signer = new MACSigner(secretKey.getBytes(StandardCharsets.UTF_8));
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    public Jwt decodeToken(String token) {
        return jwtDecoder.decode(token);
    }
}
