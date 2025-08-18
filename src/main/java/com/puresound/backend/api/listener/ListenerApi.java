package com.puresound.backend.api.listener;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.listener.ListenerResponse;
import com.puresound.backend.security.jwt.UserPrincipal;
import com.puresound.backend.service.user.listener.ListenerService;
import com.puresound.backend.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/listener")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Listener API", description = "API for Listener feature")
@Slf4j
public class ListenerApi {
    ListenerService listenerService;
    ApiResponseFactory apiResponseFactory;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ListenerResponse>> getMe(@AuthenticationPrincipal UserPrincipal principal, Locale locale) {
        String id = principal.id();
        ListenerResponse listenerResponse = listenerService.getById(id);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.GET_LISTENER_SUCCESS, listenerResponse, locale));
    }
}
