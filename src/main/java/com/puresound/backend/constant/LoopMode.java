package com.puresound.backend.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LoopMode {
    NONE,
    ONE,
    ALL;

    private final String mode;

    LoopMode() {
        this.mode = this.name().toLowerCase();
    }

    @JsonValue
    public String getMode() {
        return mode;
    }
}
