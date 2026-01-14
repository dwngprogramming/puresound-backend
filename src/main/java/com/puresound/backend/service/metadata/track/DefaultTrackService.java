package com.puresound.backend.service.metadata.track;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.dto.metadata.track.SimplifiedTrackResponse;
import com.puresound.backend.dto.metadata.track.TrackResponse;
import com.puresound.backend.dto.pagination.SPFRequest;
import com.puresound.backend.dto.pagination.SPFResponse;
import com.puresound.backend.entity.jpa.metadata.track.TrackMetadata;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.mapper.metadata.TrackMapper;
import com.puresound.backend.repository.jpa.metadata.track.TrackRepository;
import com.puresound.backend.service.image.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class DefaultTrackService implements TrackService {
    TrackRepository trackRepository;
    ImageService imageService;
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

        List<SimplifiedTrackResponse> tracks = trackMapper.toSpfSimplifiedResponses(trackMetadataPage).content();
        List<SimplifiedTrackResponse> tracksAfterAddImages = tracks.stream()
                .map(track -> {
                    List<SimplifiedArtistResponse> artistsWithImages = imageService.addImagesToSimplifiedArtists(track.artists());
                    SimplifiedAlbumResponse albumWithImages = imageService.addImagesToSimplifiedAlbum(track.album());
                    return track.withArtistsAndAlbum(artistsWithImages, albumWithImages);
                })
                .toList();

        return SPFResponse.of(tracksAfterAddImages, trackMapper.toSpfSimplifiedResponses(trackMetadataPage).paging());
    }
}
