package com.puresound.backend.api.stream;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.stream.StreamInfoResponse;
import com.puresound.backend.dto.stream.StreamSessionRequest;
import com.puresound.backend.exception.exts.UnauthorizedException;
import com.puresound.backend.security.cookie.CookieService;
import com.puresound.backend.security.jwt.JwtTokenProvider;
import com.puresound.backend.security.jwt.UserPrincipal;
import com.puresound.backend.service.subscription.listener.ListenerSubService;
import com.puresound.backend.util.ApiResponseFactory;
import com.puresound.backend.util.StreamTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/stream")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Stream API", description = "API for Streaming Music feature")
@Slf4j
public class StreamApi {
    ListenerSubService subService;
    JwtTokenProvider jwtTokenProvider;
    CookieService cookieService;
    ApiResponseFactory apiResponseFactory;

    @GetMapping(value = "/session/token")
    public ResponseEntity<ApiResponse<Void>> verifyOrCreateStreamSessionToken(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                              @CookieValue(value = "stream_session", required = false) String streamSessionCookie,
                                                                              @Value("${jwt.exp-stream-min}") long expStreamMin,
                                                                              HttpServletResponse response,
                                                                              Locale locale) {
        // If stream session cookie exists and is valid, return success response
        if (streamSessionCookie != null && jwtTokenProvider.validateToken(streamSessionCookie)) {
            return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.STREAM_SESSION_TOKEN_EXISTS, locale));
        }

        // Else, create a new stream session token and put into cookie
        String userId = userPrincipal != null ? userPrincipal.id() : "guest";
        boolean isActiveSubscription = userPrincipal != null && subService.isCurrentSubActive(userPrincipal.id());

        String streamToken = jwtTokenProvider.generateStreamToken(userId, isActiveSubscription);
        cookieService.setCookie("stream_session", streamToken, expStreamMin, response);

        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.CREATE_STREAM_SESSION_TOKEN, locale));
    }

    @GetMapping(value = "/{bitrate}/{trackId}")
    public ResponseEntity<ApiResponse<StreamInfoResponse>> streamTrack(@CookieValue(value = "stream_session") String streamSessionCookie,
                                                                       @PathVariable Integer bitrate,
                                                                       @PathVariable String trackId,
                                                                       @Value("${jwt.secret}") String jwtSecret,
                                                                       @Value("${jwt.exp-stream-url-min}") long expStreamUrlMin,
                                                                       @Value("${minio.endpoint}") String streamEndpoint,
                                                                       Locale locale) {
        if (streamSessionCookie == null)
            throw new UnauthorizedException(ApiMessage.STREAM_SESSION_INVALID, LogLevel.WARN);
        if (!jwtTokenProvider.validateToken(streamSessionCookie))
            throw new UnauthorizedException(ApiMessage.STREAM_SESSION_EXPIRED, LogLevel.INFO);
        StreamSessionRequest streamSession = jwtTokenProvider.parseStreamToken(streamSessionCookie);
        Integer acceptedBitrate = bitrate;
        if (!streamSession.premium()) {
            if (bitrate >= 192) {
                acceptedBitrate = 192;
            } else {
                acceptedBitrate = 128;
            }
        }
        long exp = Instant.now().getEpochSecond() + expStreamUrlMin * 60;
        String tokenParam = StreamTokenUtil.generateStreamingVerifyToken(trackId, acceptedBitrate, exp, jwtSecret);
        String streamUrl = String.format("%s/%d/%s/m3u8?%s", streamEndpoint, acceptedBitrate, trackId, tokenParam);
        StreamInfoResponse streamInfo = new StreamInfoResponse(streamEndpoint, streamUrl, tokenParam, exp);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.CREATE_STREAM_URL_SUCCESS, streamInfo, locale));
    }
}
