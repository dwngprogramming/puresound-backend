package com.puresound.backend.constant.user;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.exception.exts.BadRequestException;

public enum UserType {
    LISTENER,
    ARTIST,
    STAFF;

    public static UserType fromString(String value) {
        if (value == null) return null;

        try {
            return UserType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(ApiMessage.UNKNOWN_USER_TYPE);
        }
    }
}
