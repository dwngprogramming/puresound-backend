package com.puresound.backend.service.subscription.listener;

import com.puresound.backend.dto.subscription.BasicSubResponse;
import com.puresound.backend.dto.subscription.listener.ListenerSubCache;
import com.puresound.backend.dto.subscription.listener.ListenerSubResponse;
import com.puresound.backend.entity.subscription.listener.ListenerSubPeriod;
import com.puresound.backend.mapper.subscription.listener.ListenerSubMapper;
import com.puresound.backend.mapper.subscription.listener.ListenerSubPeriodMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class DefaultListenerSubService implements ListenerSubService {
    ListenerSubPeriodService periodService;
    ListenerSubCacheService cacheService;
    ListenerSubPeriodMapper periodMapper;
    ListenerSubMapper subMapper;

    @Override
    public ListenerSubResponse getCurrentDetailByListenerId(String listenerId) {
        // Try to get from cache first
        ListenerSubCache cached = cacheService.get(listenerId);
        if (cached != null) {
            return subMapper.toListenerSubscriptionResponse(cached);
        }

        // Fallback to DB if cache miss
        ListenerSubPeriod period = periodService.getCurrentByListenerId(listenerId);
        if (period != null) {
            ListenerSubResponse response = periodMapper.toResponse(period);
            // Save to cache for future requests
            cacheService.create(listenerId, response);
            return response;
        }
        return ListenerSubResponse.empty();
    }

    @Override
    public BasicSubResponse getCurrentBasicByListenerId(String listenerId) {
        ListenerSubResponse detail = getCurrentDetailByListenerId(listenerId);
        return subMapper.toBasicSubscriptionResponse(detail);
    }
}
