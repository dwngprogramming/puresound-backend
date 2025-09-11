package com.puresound.backend.dto.location;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record ReverseGeocodingRequest(
        @NotNull(message = "{LAT_NOT_NULL}")
        @DecimalMin(value = "-90.0", message = "{LAT_INVALID}")
        @DecimalMax(value = "90.0", message = "{LAT_INVALID}")
        Double latitude,

        @NotNull(message = "{LON_NOT_NULL}")
        @DecimalMin(value = "-180.0", message = "{LON_INVALID}")
        @DecimalMax(value = "180.0", message = "{LON_INVALID}")
        Double longitude
) {
}
