package com.puresound.backend.mapper.subscription.listener;

import com.puresound.backend.dto.subscription.listener.ListenerSubscriptionResponse;
import com.puresound.backend.entity.subscription.listener.ListenerSubscription;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class, uses = {ListenerSubscriptionPlanMapper.class})
public interface ListenerSubscriptionMapper {
    ListenerSubscriptionResponse toListenerSubscriptionResponse(ListenerSubscription listenerSubscription);
}
