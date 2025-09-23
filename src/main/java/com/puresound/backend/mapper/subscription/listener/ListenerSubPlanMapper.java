package com.puresound.backend.mapper.subscription.listener;

import com.puresound.backend.dto.subscription.listener.ListenerSubPlanResponse;
import com.puresound.backend.entity.subscription.listener.ListenerSubPlan;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface ListenerSubPlanMapper {
    ListenerSubPlanResponse toPlanResponse(ListenerSubPlan plan);

    List<ListenerSubPlanResponse> toPlanResponses(Collection<ListenerSubPlan> plans);
}
