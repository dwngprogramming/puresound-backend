package com.puresound.backend.constant.api;

public enum ApiMessage {
    // Success + Logging
    GET_LISTENER_SUCCESS,
    LOGIN_SUCCESS,
    EMAIL_EXISTS,

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
    FEATURE_IN_FUTURE,
    MISSING_REFRESH_TOKEN,
    UNAUTHENTICATED,
    INVALID_EXCHANGE_CODE,
    LOGOUT_SUCCESS,
    LINKED_TO_OAUTH2,

    // Only for logging, don't show to user & don't create message locale, or show Internal Server Error
    INVALID_FORMAT_TOKEN,
    INVALID_OAUTH2_PROVIDER
}
