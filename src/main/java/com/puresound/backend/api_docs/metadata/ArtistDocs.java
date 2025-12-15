package com.puresound.backend.api_docs.metadata;

import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.dto.pagination.SPFResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

@Tag(name = "Artist Metadata API", description = "API for Artist Metadata feature")
public interface ArtistDocs {

    @Operation(summary = "Get Featured Artists",
            description = """
                    Retrieve a paginated list of featured artists (10 track/page).</br>
                    Default page is `1`, sort by `popularity` and sort direction is `DESC`.</br>
                    <b>Note:</b> Request from user when using pagination will be transformed to `SPFRequest`.</br>
                    """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Get featured artists successfully"),
    })
    ResponseEntity<ApiResponse<SPFResponse<SimplifiedArtistResponse>>> getFeaturedArtists(Integer page, Locale locale);
}
