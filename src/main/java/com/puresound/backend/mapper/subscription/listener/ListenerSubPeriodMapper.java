package com.puresound.backend.mapper.subscription.listener;

import com.puresound.backend.dto.subscription.listener.ListenerSubResponse;
import com.puresound.backend.entity.subscription.listener.ListenerSubPeriod;
import com.puresound.backend.mapper.GlobalMapperConfig;
import com.puresound.backend.mapper.listener.ListenerMapper;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class, uses = {ListenerSubMapper.class, ListenerMapper.class})
@DecoratedWith(ListenerSubPeriodDecorator.class)
public interface ListenerSubPeriodMapper {
    ListenerSubResponse toResponse(ListenerSubPeriod period);
}
