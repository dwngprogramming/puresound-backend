package com.puresound.backend.constant.metadata;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum Bitrate {
    KBPS_320(320),
    KBPS_192(192),
    KBPS_128(128);

    int bitrate;

    Bitrate(int bitrate) {
        this.bitrate = bitrate;
    }
}
