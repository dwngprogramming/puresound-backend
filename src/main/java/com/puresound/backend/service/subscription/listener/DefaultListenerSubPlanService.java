package com.puresound.backend.service.subscription.listener;

import com.puresound.backend.dto.subscription.listener.ListenerSubPlanResponse;
import com.puresound.backend.mapper.subscription.listener.ListenerSubPlanMapper;
import com.puresound.backend.repository.subscription.listener.ListenerPlanRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class DefaultListenerSubPlanService implements ListenerSubPlanService {
    ListenerPlanRepository repository;
    ListenerSubPlanMapper mapper;

    @Override
    public List<ListenerSubPlanResponse> getAllPlans(boolean isFirstSubscription) {
        if (isFirstSubscription) {
            return mapper.toPlanResponses(repository.findForNeverSubscribed());
        }
        return mapper.toPlanResponses(repository.findForCommonSubscribed());
    }
}
