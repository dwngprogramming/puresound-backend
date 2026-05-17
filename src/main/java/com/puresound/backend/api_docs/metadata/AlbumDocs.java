package com.puresound.backend.api_docs.metadata;

import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.metadata.album.AlbumResponse;
import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.pagination.SPFResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

@Tag(name = "Album Metadata API", description = "API for Album Metadata feature")
public interface AlbumDocs {

    @Operation(summary = "Get Popular Albums",
            description = """
                    Retrieve a paginated list of popular albums (10 albums/page).</br>
                    Default page is `1`, sort by `popularity` and sort direction is `DESC`.</br>
                    <b>Note:</b> Request from user when using pagination will be transformed to `SPFRequest`.</br>
                    """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Get popular albums successfully"),
    })
    ResponseEntity<ApiResponse<SPFResponse<SimplifiedAlbumResponse>>> getPopularAlbums(Integer page, Locale locale);

    @Operation(summary = "Get Album by ID",
            description = """
                    Retrieve detailed information about an album using its unique identifier.</br>
                    The album ID must be a valid 26-character ULID.</br>
                    """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Get album successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid album ID format",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Album not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
    })
    ResponseEntity<ApiResponse<AlbumResponse>> getAlbumById(String id, Locale locale);
}
