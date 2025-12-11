package com.puresound.backend.service.metadata.artist;

import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.dto.pagination.SPFRequest;
import com.puresound.backend.dto.pagination.SPFResponse;

public interface ArtistService {
    SPFResponse<SimplifiedArtistResponse> getFeaturedArtists(SPFRequest request);
}
