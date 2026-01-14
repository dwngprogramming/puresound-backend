package com.puresound.backend.service.metadata.artist;

import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.dto.pagination.SPFRequest;
import com.puresound.backend.dto.pagination.SPFResponse;
import com.puresound.backend.entity.jpa.metadata.artist.ArtistMetadata;
import com.puresound.backend.mapper.metadata.ArtistMapper;
import com.puresound.backend.repository.jpa.metadata.artist.ArtistRepository;
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
public class DefaultArtistService implements ArtistService {
    ArtistRepository artistRepository;
    ImageService imageService;
    ArtistMapper artistMapper;

    @Override
    public SPFResponse<SimplifiedArtistResponse> getFeaturedArtists(SPFRequest request) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(), request.sort());
        Page<ArtistMetadata> artistMetadataPage = artistRepository.findAll(pageable);

        List<SimplifiedArtistResponse> artists = artistMapper.toSpfSimplifiedResponse(artistMetadataPage).content();

        List<SimplifiedArtistResponse> artistsWithImages = imageService.addImagesToSimplifiedArtists(artists);
        return SPFResponse.of(artistsWithImages, artistMapper.toSpfSimplifiedResponse(artistMetadataPage).paging());
    }
}
