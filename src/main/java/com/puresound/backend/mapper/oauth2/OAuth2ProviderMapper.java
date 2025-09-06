package com.puresound.backend.mapper.oauth2;

import com.puresound.backend.dto.auth.OAuth2ProviderRequest;
import com.puresound.backend.repository.entity.user.oauth2.OAuth2Provider;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
@DecoratedWith(OAuth2ProviderMapperDecorator.class)
public interface OAuth2ProviderMapper {
    OAuth2Provider createOAuth2Provider(OAuth2ProviderRequest request);
}
