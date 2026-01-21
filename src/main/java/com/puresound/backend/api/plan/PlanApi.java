package com.puresound.backend.api.plan;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.subscription.listener.ListenerSubPlanResponse;
import com.puresound.backend.service.user.listener.ListenerService;
import com.puresound.backend.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/plans")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Plan API", description = "API for Plan feature")
public class PlanApi {
    // TODO: Separate plan logic from Listener context
    ListenerService listenerService;
    ApiResponseFactory apiResponseFactory;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ListenerSubPlanResponse>>> getSubscriptionPlans(Locale locale) {
        boolean isFirstSubscription = false; // TODO: Temporary set to false, need to check from user's subscription history
        List<ListenerSubPlanResponse> plans = listenerService.getAllSubscriptionPlans(isFirstSubscription);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.GET_ALL_PLANS_SUCCESS, plans, locale));
    }
}
