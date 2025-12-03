package com.puresound.backend.api.metadata;

import com.puresound.backend.api_docs.metadata.TrackDocs;
import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.metadata.track.SimplifiedTrackResponse;
import com.puresound.backend.dto.metadata.track.TrackResponse;
import com.puresound.backend.dto.pagination.SPFRequest;
import com.puresound.backend.dto.pagination.SPFResponse;
import com.puresound.backend.service.metadata.track.TrackService;
import com.puresound.backend.util.ApiResponseFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/tracks")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrackApi implements TrackDocs {
    final TrackService trackService;
    final ApiResponseFactory apiResponseFactory;

    @Value("${paging-size.default}")
    private Integer defaultSize;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TrackResponse>> getTrackById(@PathVariable String id, Locale locale) {
        TrackResponse response = trackService.getTrackById(id);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.GET_TRACK_SUCCESS, response, locale));
    }

    @GetMapping(value = "/popular-tracks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<SPFResponse<SimplifiedTrackResponse>>> getPopularTracks(@RequestParam(defaultValue = "1") Integer page,
                                                                                              Locale locale) {
        Integer sanitizedPage = Math.max(page, 1);
        SPFRequest request = SPFRequest.withPopularityDefaultSort(sanitizedPage, defaultSize);
        SPFResponse<SimplifiedTrackResponse> response = trackService.getPopularTracks(request);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.GET_TRACK_SUCCESS, response, locale));
    }
}
