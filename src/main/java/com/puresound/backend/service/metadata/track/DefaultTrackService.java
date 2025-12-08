package com.puresound.backend.service.metadata.track;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.dto.metadata.track.SimplifiedTrackResponse;
import com.puresound.backend.dto.metadata.track.TrackResponse;
import com.puresound.backend.dto.pagination.SPFRequest;
import com.puresound.backend.dto.pagination.SPFResponse;
import com.puresound.backend.entity.jpa.metadata.track.TrackMetadata;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.mapper.metadata.TrackMapper;
import com.puresound.backend.repository.jpa.metadata.track.TrackRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class DefaultTrackService implements TrackService {
    TrackRepository trackRepository;
    TrackMapper trackMapper;

    @Override
    public TrackResponse getTrackById(String id) {
        TrackMetadata trackMetadata = trackRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ApiMessage.TRACK_NOT_FOUND, LogLevel.INFO));
        return trackMapper.toResponse(trackMetadata);
    }

    @Override
    public SPFResponse<SimplifiedTrackResponse> getPopularTracks(SPFRequest request) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(), request.sort());
        Page<TrackMetadata> trackMetadataPage = trackRepository.findAll(pageable);
        return trackMapper.toSpfSimplifiedResponses(trackMetadataPage, request.sort());
    }
}
