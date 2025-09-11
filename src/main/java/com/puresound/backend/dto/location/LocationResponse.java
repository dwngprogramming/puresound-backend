package com.puresound.backend.dto.location;

public record LocationResponse(
        String district,
        String province,
        String countryCode
) {
}
