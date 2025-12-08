package com.puresound.backend.api_docs.metadata;

import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.metadata.track.SimplifiedTrackResponse;
import com.puresound.backend.dto.pagination.SPFResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

@Tag(name = "Track Metadata API", description = "API for Track Metadata feature")
public interface TrackDocs {

    @Operation(summary = "Get Popular Tracks",
            description = "Retrieve a paginated list of popular tracks (10 track/page request).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Get popular tracks successfully"),
    })
    ResponseEntity<ApiResponse<SPFResponse<SimplifiedTrackResponse>>> getPopularTracks(Integer page, Locale locale);
}
