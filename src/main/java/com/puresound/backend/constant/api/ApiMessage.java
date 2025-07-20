package com.puresound.backend.constant.api;

public enum ApiMessage {
    // Success
    GET_LISTENER_SUCCESS,
    LOGIN_SUCCESS,

    // Fail
    INVALID_REQUEST,
    MISMATCH_REQUEST,
    LOGIN_WRONG_INFO,
    SYSTEM_RESOURCE_NOT_FOUND,
    LISTENER_NOT_FOUND,
    UNKNOWN_USER_TYPE,
    INVALID_TOKEN,
    INTERNAL_SERVER_ERROR
}
