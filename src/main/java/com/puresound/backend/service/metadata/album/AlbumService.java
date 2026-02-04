package com.puresound.backend.service.metadata.album;

import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.pagination.SPFRequest;
import com.puresound.backend.dto.pagination.SPFResponse;

public interface AlbumService {
    SPFResponse<SimplifiedAlbumResponse> getPopularAlbums(SPFRequest request);
}
