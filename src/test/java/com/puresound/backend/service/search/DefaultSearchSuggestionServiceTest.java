package com.puresound.backend.service.search;

import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.dto.metadata.track.SimplifiedTrackResponse;
import com.puresound.backend.dto.search.SearchSuggestionResponse;
import com.puresound.backend.entity.jpa.metadata.album.AlbumMetadata;
import com.puresound.backend.entity.jpa.metadata.artist.ArtistMetadata;
import com.puresound.backend.entity.jpa.metadata.track.TrackMetadata;
import com.puresound.backend.mapper.metadata.AlbumMapper;
import com.puresound.backend.mapper.metadata.ArtistMapper;
import com.puresound.backend.mapper.metadata.TrackMapper;
import com.puresound.backend.repository.jpa.metadata.album.AlbumRepository;
import com.puresound.backend.repository.jpa.metadata.artist.ArtistRepository;
import com.puresound.backend.repository.jpa.metadata.track.TrackRepository;
import com.puresound.backend.service.image.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultSearchSuggestionServiceTest {

    @Mock
    TrackRepository trackRepository;

    @Mock
    AlbumRepository albumRepository;

    @Mock
    ArtistRepository artistRepository;

    @Mock
    TrackMapper trackMapper;

    @Mock
    AlbumMapper albumMapper;

    @Mock
    ArtistMapper artistMapper;

    @Mock
    ImageService imageService;

    @InjectMocks
    SearchSuggestionService searchSuggestionService;

    @Test
    void getSuggestionsReturnsGroupedResultsWithTrimmedKeywordAndCappedLimit() {
        TrackMetadata trackMetadata = new TrackMetadata();
        AlbumMetadata albumMetadata = new AlbumMetadata();
        ArtistMetadata artistMetadata = new ArtistMetadata();
        SimplifiedArtistResponse artist = SimplifiedArtistResponse.builder().id("artist-id").stageName("OPALS").build();
        SimplifiedAlbumResponse album = SimplifiedAlbumResponse.builder().id("album-id").name("Love Album").build();
        SimplifiedTrackResponse track = SimplifiedTrackResponse.builder()
                .id("track-id")
                .title("Love Song")
                .artists(List.of(artist))
                .album(album)
                .build();

        when(trackRepository.findByTitleContainingIgnoreCase(eq("love"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(trackMetadata)));
        when(albumRepository.findByNameContainingIgnoreCase(eq("love"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(albumMetadata)));
        when(artistRepository.findByStageNameContainingIgnoreCase(eq("love"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(artistMetadata)));
        when(trackMapper.toSimplifiedResponse(trackMetadata)).thenReturn(track);
        when(albumMapper.toSimplifiedAlbumResponse(albumMetadata)).thenReturn(album);
        when(artistMapper.toSimplifiedResponse(artistMetadata)).thenReturn(artist);
        when(imageService.addImagesToSimplifiedArtists(track.artists())).thenReturn(track.artists());
        when(imageService.addImagesToSimplifiedAlbum(album)).thenReturn(album);
        when(imageService.addImagesToSimplifiedArtist(artist)).thenReturn(artist);

        SearchSuggestionResponse response = searchSuggestionService.getSuggestions("  love  ", 50);

        assertThat(response.keyword()).isEqualTo("love");
        assertThat(response.tracks()).containsExactly(track);
        assertThat(response.albums()).containsExactly(album);
        assertThat(response.artists()).containsExactly(artist);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(trackRepository).findByTitleContainingIgnoreCase(eq("love"), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort().getOrderFor("popularity").isDescending()).isTrue();
    }

    @Test
    void getSuggestionsReturnsEmptyListsWhenNothingMatches() {
        when(trackRepository.findByTitleContainingIgnoreCase(eq("unknown"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));
        when(albumRepository.findByNameContainingIgnoreCase(eq("unknown"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));
        when(artistRepository.findByStageNameContainingIgnoreCase(eq("unknown"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        SearchSuggestionResponse response = searchSuggestionService.getSuggestions("unknown", 0);

        assertThat(response.keyword()).isEqualTo("unknown");
        assertThat(response.tracks()).isEmpty();
        assertThat(response.albums()).isEmpty();
        assertThat(response.artists()).isEmpty();

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(trackRepository).findByTitleContainingIgnoreCase(eq("unknown"), pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(1);
    }
}
