package com.puresound.backend.api.listener;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.listener.ListenerResponse;
import com.puresound.backend.dto.subscription.BasicSubResponse;
import com.puresound.backend.dto.subscription.listener.ListenerSubPlanResponse;
import com.puresound.backend.dto.subscription.listener.ListenerSubResponse;
import com.puresound.backend.entity.redis.listener_collection.ListenerCollectionCache;
import com.puresound.backend.exception.exts.UnauthorizedException;
import com.puresound.backend.security.jwt.UserPrincipal;
import com.puresound.backend.service.cache.ListenerCollectionService;
import com.puresound.backend.service.user.listener.ListenerService;
import com.puresound.backend.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/listener")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Listener API", description = "API for Listener feature")
@Slf4j
public class ListenerApi {
    ListenerService listenerService;
    ListenerCollectionService listenerCollectionService;
    ApiResponseFactory apiResponseFactory;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ListenerResponse>> getMe(@AuthenticationPrincipal UserPrincipal principal, Locale locale) {
        String id = principal.id();
        ListenerResponse listenerResponse = listenerService.getById(id);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.GET_LISTENER_SUCCESS, listenerResponse, locale));
    }

    @GetMapping("/subscription/base")
    public ResponseEntity<ApiResponse<BasicSubResponse>> getMyBasicSubscription(@AuthenticationPrincipal UserPrincipal principal, Locale locale) {
        String id = principal.id();
        BasicSubResponse subscription = listenerService.getCurrentBaseSubscription(id);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.GET_MY_SUBSCRIPTION_SUCCESS, subscription, locale));
    }

    @GetMapping("/subscription/detail")
    public ResponseEntity<ApiResponse<ListenerSubResponse>> getMyDetailSubscription(@AuthenticationPrincipal UserPrincipal principal, Locale locale) {
        String id = principal.id();
        ListenerSubResponse subscription = listenerService.getCurrentDetailSubscription(id);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.GET_MY_SUBSCRIPTION_SUCCESS, subscription, locale));
    }

    @GetMapping("/plans")
    public ResponseEntity<ApiResponse<List<ListenerSubPlanResponse>>> getSubscriptionPlans(Locale locale) {
        boolean isFirstSubscription = false; // TODO: Temporary set to false, need to check from user's subscription history
        List<ListenerSubPlanResponse> plans = listenerService.getAllSubscriptionPlans(isFirstSubscription);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.GET_ALL_PLANS_SUCCESS, plans, locale));
    }

    @PostMapping("/collection/create")
    public ResponseEntity<ApiResponse<Void>> createListenerCollection(@AuthenticationPrincipal UserPrincipal principal,
                                                                      @RequestBody @Valid ListenerCollectionCache collectionCache,
                                                                      Locale locale) {
        if (principal == null || !principal.id().equals(collectionCache.getListenerId()))
            throw new UnauthorizedException(ApiMessage.UNAUTHENTICATED, LogLevel.WARN);
        listenerCollectionService.create(collectionCache);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.CREATE_LISTENER_COLLECTION_SUCCESS, locale));
    }

    @PutMapping("/collection/update")
    public ResponseEntity<ApiResponse<ListenerCollectionCache>> updateListenerCollection(@AuthenticationPrincipal UserPrincipal principal,
                                                                                       @RequestBody @Valid ListenerCollectionCache collectionCache,
                                                                                       Locale locale) {
        if (principal == null || !principal.id().equals(collectionCache.getListenerId()))
            throw new UnauthorizedException(ApiMessage.UNAUTHENTICATED, LogLevel.WARN);
        ListenerCollectionCache updatedCollection = listenerCollectionService.update(collectionCache);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.CREATE_LISTENER_COLLECTION_SUCCESS, updatedCollection, locale));
    }
}
