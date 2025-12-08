package com.puresound.backend.service.metadata.track;

import com.puresound.backend.dto.metadata.track.TrackResponse;

public interface TrackService {
    TrackResponse getTrackById(String id);
}
