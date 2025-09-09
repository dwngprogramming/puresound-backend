package com.puresound.backend.constant.payment;

import lombok.Getter;

@Getter
public enum TransactionPrefix {
    LISTENER_INDIVIDUAL("PSLI_"),
    LISTENER_STUDENT("PSLS_");

    private final String prefix;

    TransactionPrefix(String prefix) {
        this.prefix = prefix;
    }
}
