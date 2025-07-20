package com.puresound.backend.mapper.listener;

import com.puresound.backend.entity.user.listener.Listener;
import com.puresound.backend.mapper.GlobalMapperConfig;
import com.puresound.backend.security.local.LocalAuthentication;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
@DecoratedWith(ListenerMapperDecorator.class)
public interface ListenerMapper {
    LocalAuthentication toLocalAuthentication(Listener listener);
}
