package com.puresound.backend.mapper.subscription.listener;

import com.puresound.backend.dto.subscription.listener.ListenerSubCache;
import com.puresound.backend.dto.subscription.listener.ListenerSubResponse;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface ListenerSubCacheMapper {
    ListenerSubResponse toResponse(ListenerSubCache cache);

    ListenerSubCache toCache(ListenerSubResponse response);
}
