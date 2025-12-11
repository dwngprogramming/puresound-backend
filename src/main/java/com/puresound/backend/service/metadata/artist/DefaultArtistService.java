package com.puresound.backend.service.metadata.artist;

import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.dto.pagination.SPFRequest;
import com.puresound.backend.dto.pagination.SPFResponse;
import com.puresound.backend.entity.jpa.metadata.artist.ArtistMetadata;
import com.puresound.backend.mapper.metadata.ArtistMapper;
import com.puresound.backend.repository.jpa.metadata.artist.ArtistRepository;
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
public class DefaultArtistService implements ArtistService {
    ArtistRepository artistRepository;
    ArtistMapper artistMapper;

    @Override
    public SPFResponse<SimplifiedArtistResponse> getFeaturedArtists(SPFRequest request) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(), request.sort());
        Page<ArtistMetadata> artistMetadataPage = artistRepository.findAll(pageable);
        return artistMapper.toSpfSimplifiedResponse(artistMetadataPage);
    }
}
