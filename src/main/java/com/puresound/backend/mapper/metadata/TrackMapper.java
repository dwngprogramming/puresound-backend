package com.puresound.backend.mapper.metadata;

import com.puresound.backend.dto.metadata.track.SimplifiedTrackResponse;
import com.puresound.backend.dto.metadata.track.TrackResponse;
import com.puresound.backend.dto.pagination.PagingResponse;
import com.puresound.backend.dto.pagination.SPFResponse;
import com.puresound.backend.entity.jpa.metadata.track.TrackMetadata;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class, uses = {
        ArtistMapper.class,
        AlbumMapper.class,
        GenreMapper.class
})
@DecoratedWith(TrackDecorator.class)
public interface TrackMapper {

    @Mapping(target = "album", ignore = true)
    @Mapping(target = "artists", ignore = true)
    @Mapping(target = "availableBitrates", ignore = true)
    TrackResponse toResponse(TrackMetadata trackMetadata);

    @Mapping(target = "album", ignore = true)
    @Mapping(target = "artists", ignore = true)
    SimplifiedTrackResponse toSimplifiedResponse(TrackMetadata trackMetadata);

    default SPFResponse<SimplifiedTrackResponse> toSpfSimplifiedResponses(Page<TrackMetadata> trackMetadataPage, Sort sort) {
        List<SimplifiedTrackResponse> content = trackMetadataPage
                .map(this::toSimplifiedResponse)
                .getContent();

        String sortBy = sort.isSorted() ? sort.iterator().next().getProperty() : null;
        String sortDirection = sort.isSorted() ? sort.iterator().next().getDirection().name() : null;

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(trackMetadataPage.getNumber() + 1)
                .size(trackMetadataPage.getSize())
                .first(trackMetadataPage.isFirst())
                .last(trackMetadataPage.isLast())
                .totalElements(trackMetadataPage.getTotalElements())
                .totalPages(trackMetadataPage.getTotalPages())
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        return SPFResponse.of(content, pagingResponse);
    }
}
