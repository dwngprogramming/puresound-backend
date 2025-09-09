package com.puresound.backend.mapper.subscription.listener;

import com.puresound.backend.dto.subscription.listener.ListenerSubscriptionPlanResponse;
import com.puresound.backend.entity.subscription.listener.ListenerSubscriptionPlan;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface ListenerSubscriptionPlanMapper {
    ListenerSubscriptionPlanResponse toResponse(ListenerSubscriptionPlan plan);
}
