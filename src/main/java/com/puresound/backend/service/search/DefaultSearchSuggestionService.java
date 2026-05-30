package com.puresound.backend.service.search;

import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.dto.metadata.track.SimplifiedTrackResponse;
import com.puresound.backend.dto.search.SearchSuggestionResponse;
import com.puresound.backend.mapper.metadata.AlbumMapper;
import com.puresound.backend.mapper.metadata.ArtistMapper;
import com.puresound.backend.mapper.metadata.TrackMapper;
import com.puresound.backend.repository.jpa.metadata.album.AlbumRepository;
import com.puresound.backend.repository.jpa.metadata.artist.ArtistRepository;
import com.puresound.backend.repository.jpa.metadata.track.TrackRepository;
import com.puresound.backend.service.image.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class DefaultSearchSuggestionService implements SearchSuggestionService {
    private static final int DEFAULT_LIMIT = 5;
    private static final int MAX_LIMIT = 10;

    TrackRepository trackRepository;
    AlbumRepository albumRepository;
    ArtistRepository artistRepository;
    TrackMapper trackMapper;
    AlbumMapper albumMapper;
    ArtistMapper artistMapper;
    ImageService imageService;

    @Override
    @Transactional(readOnly = true)
    public SearchSuggestionResponse getSuggestions(String keyword, Integer limit) {
        String normalizedKeyword = keyword.trim();
        Pageable pageable = PageRequest.of(0, sanitizeLimit(limit), Sort.by(Sort.Direction.DESC, "popularity"));

        List<SimplifiedTrackResponse> tracks = trackRepository
                .findByTitleContainingIgnoreCase(normalizedKeyword, pageable)
                .map(trackMapper::toSimplifiedResponse)
                .map(this::addTrackImages)
                .getContent();

        List<SimplifiedAlbumResponse> albums = albumRepository
                .findByNameContainingIgnoreCase(normalizedKeyword, pageable)
                .map(albumMapper::toSimplifiedAlbumResponse)
                .map(imageService::addImagesToSimplifiedAlbum)
                .getContent();

        List<SimplifiedArtistResponse> artists = artistRepository
                .findByStageNameContainingIgnoreCase(normalizedKeyword, pageable)
                .map(artistMapper::toSimplifiedResponse)
                .map(imageService::addImagesToSimplifiedArtist)
                .getContent();

        return SearchSuggestionResponse.of(normalizedKeyword, tracks, albums, artists);
    }

    private int sanitizeLimit(Integer limit) {
        if (limit == null) {
            return DEFAULT_LIMIT;
        }
        return Math.min(Math.max(limit, 1), MAX_LIMIT);
    }

    private SimplifiedTrackResponse addTrackImages(SimplifiedTrackResponse track) {
        List<SimplifiedArtistResponse> artistsWithImages = imageService.addImagesToSimplifiedArtists(track.artists());
        SimplifiedAlbumResponse albumWithImages = imageService.addImagesToSimplifiedAlbum(track.album());
        return track.withArtistsAndAlbum(artistsWithImages, albumWithImages);
    }
}
