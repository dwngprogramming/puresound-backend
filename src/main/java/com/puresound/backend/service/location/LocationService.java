package com.puresound.backend.service.location;

import com.puresound.backend.dto.location.LocationResponse;
import com.puresound.backend.dto.location.ReverseGeocodingRequest;

public interface LocationService {
    LocationResponse reverseGeocode(ReverseGeocodingRequest request);
}
