package com.puresound.backend.mapper.listener;

import com.puresound.backend.dto.auth.RefreshAuthentication;
import com.puresound.backend.dto.listener.ListenerOAuthInfoRequest;
import com.puresound.backend.dto.listener.ListenerRegisterRequest;
import com.puresound.backend.dto.listener.ListenerResponse;
import com.puresound.backend.entity.listener.Listener;
import com.puresound.backend.mapper.GlobalMapperConfig;
import com.puresound.backend.mapper.subscription.listener.ListenerSubscriptionMapper;
import com.puresound.backend.security.local.LocalAuthentication;
import com.puresound.backend.security.oauth2.OAuth2Authentication;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class, uses = {ListenerSubscriptionMapper.class})
@DecoratedWith(ListenerMapperDecorator.class)
public interface ListenerMapper {
    LocalAuthentication toLocalAuthentication(Listener listener);

    OAuth2Authentication toOAuth2Authentication(Listener listener);

    Listener toListener(ListenerOAuthInfoRequest request);

    RefreshAuthentication toRefreshAuthentication(Listener listener);

    Listener toListener(ListenerRegisterRequest request);

    ListenerResponse toListenerResponse(Listener listener);
}
