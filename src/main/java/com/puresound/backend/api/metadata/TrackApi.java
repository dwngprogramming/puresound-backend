package com.puresound.backend.api.metadata;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.metadata.track.TrackResponse;
import com.puresound.backend.service.metadata.track.TrackService;
import com.puresound.backend.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/tracks")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Track Metadata API", description = "API for Track Metadata feature")
@Slf4j
public class TrackApi {
    TrackService trackService;
    ApiResponseFactory apiResponseFactory;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TrackResponse>> getTrackById(@PathVariable String id, Locale locale) {
        TrackResponse response = trackService.getTrackById(id);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.GET_TRACK_SUCCESS, response, locale));
    }
}
