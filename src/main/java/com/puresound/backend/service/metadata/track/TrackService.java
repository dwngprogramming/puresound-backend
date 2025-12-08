package com.puresound.backend.service.metadata.track;

import com.puresound.backend.dto.metadata.track.SimplifiedTrackResponse;
import com.puresound.backend.dto.metadata.track.TrackResponse;
import com.puresound.backend.dto.pagination.SPFRequest;
import com.puresound.backend.dto.pagination.SPFResponse;

public interface TrackService {
    TrackResponse getTrackById(String id);

    SPFResponse<SimplifiedTrackResponse> getPopularTracks(SPFRequest request);
}
