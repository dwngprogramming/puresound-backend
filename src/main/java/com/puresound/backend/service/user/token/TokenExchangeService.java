package com.puresound.backend.service.user.token;

import com.puresound.backend.dto.auth.CodeExchange;

import java.util.Optional;

public interface TokenExchangeService {
    void storeCode(String code, CodeExchange request);
    Optional<CodeExchange> consumeCode(String code);
}
