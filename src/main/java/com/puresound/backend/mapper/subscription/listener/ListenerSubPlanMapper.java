package com.puresound.backend.mapper.subscription.listener;

import com.puresound.backend.dto.subscription.listener.ListenerSubPlanResponse;
import com.puresound.backend.entity.subscription.listener.ListenerSubPlan;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface ListenerSubPlanMapper {
    ListenerSubPlanResponse toResponse(ListenerSubPlan plan);
}
