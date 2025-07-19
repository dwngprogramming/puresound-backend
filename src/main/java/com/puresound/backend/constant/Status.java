package com.puresound.backend.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum Status {
    ACTIVE("Active"),
    LOCKED("Locked");

    String value;

    Status(String value) {
        this.value = value;
    }
}
