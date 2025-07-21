package com.puresound.backend.constant.api;

public enum ApiMessage {
    // Success + Logging
    GET_LISTENER_SUCCESS,
    LOGIN_SUCCESS,

    // Fail + Logging
    INVALID_REQUEST,
    MISMATCH_REQUEST,
    LOGIN_WRONG_INFO,
    SYSTEM_RESOURCE_NOT_FOUND,
    LISTENER_NOT_FOUND,
    UNKNOWN_USER_TYPE,
    INVALID_TOKEN,
    INTERNAL_SERVER_ERROR,
    INVALID_UOE_FORMAT,

    // Only for logging, don't show to user & don't create message locale
    INVALID_FORMAT_TOKEN,

}
