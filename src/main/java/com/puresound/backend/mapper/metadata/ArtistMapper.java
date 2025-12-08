package com.puresound.backend.mapper.metadata;

import com.puresound.backend.dto.metadata.artist.ArtistResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.entity.jpa.metadata.artist.ArtistMetadata;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface ArtistMapper {
    ArtistResponse toResponse(ArtistMetadata artist);

    List<ArtistResponse> toResponses(List<ArtistMetadata> artists);

    SimplifiedArtistResponse toSimplifiedResponse(ArtistMetadata artist);
}
