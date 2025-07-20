package com.puresound.backend.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.constant.user.UserType;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.security.local.UserPrincipal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    String secretKey;

    @Value("${jwt.exp-at-min}")
    long expAtMin;

    @Value("${jwt.exp-rt-min}")
    long expRtMin;

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
                    .claim("userType", userPrincipal.userType().name())
                    .claim("roles", new ArrayList<>(userPrincipal.roles()))
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

    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes(StandardCharsets.UTF_8));

            // Verify signature
            if (!signedJWT.verify(verifier)) {
                return false;
            }

            // Check expiration
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expirationTime != null && expirationTime.after(new Date());

        } catch (Exception e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new BadRequestException(ApiMessage.INVALID_TOKEN, LogLevel.WARN);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract user ID from token", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return (List<String>) signedJWT.getJWTClaimsSet().getClaim("roles");
        } catch (ParseException e) {
            throw new BadRequestException(ApiMessage.INVALID_TOKEN, LogLevel.WARN);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public UserPrincipal createUserPrincipalFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            String userId = claims.getSubject();
            String userType = claims.getStringClaim("userType");
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.getClaim("roles");

            UserType userTypeEnum = UserType.valueOf(userType);
            return new UserPrincipal(userId, userTypeEnum, new ArrayList<>(roles));
        } catch (ParseException e) {
            throw new BadRequestException(ApiMessage.INVALID_TOKEN, LogLevel.WARN);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create UserPrincipal from token", e);
        }
    }
}
