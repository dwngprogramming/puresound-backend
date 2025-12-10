package com.puresound.backend.api.metadata;

import com.puresound.backend.api_docs.metadata.ArtistDocs;
import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.dto.pagination.SPFRequest;
import com.puresound.backend.dto.pagination.SPFResponse;
import com.puresound.backend.service.metadata.artist.ArtistService;
import com.puresound.backend.util.ApiResponseFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/tracks")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArtistApi implements ArtistDocs {
    final ArtistService artistService;
    final ApiResponseFactory apiResponseFactory;

    @Value("${paging-size.default}")
    Integer defaultSize;


    @GetMapping(value = "/featured-artists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<SPFResponse<SimplifiedArtistResponse>>> getFeaturedArtists(@RequestParam(defaultValue = "1") Integer page,
                                                                                                 Locale locale) {
        Integer sanitizedPage = Math.max(page, 1);
        SPFRequest request = SPFRequest.withPopularityDefaultSort(sanitizedPage, defaultSize);
        SPFResponse<SimplifiedArtistResponse> response = artistService.getFeaturedArtists(request);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.GET_ARTIST_SUCCESS, response, locale));
    }
}
