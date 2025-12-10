package com.puresound.backend.mapper.metadata;

import com.puresound.backend.dto.metadata.artist.ArtistResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.dto.pagination.PagingResponse;
import com.puresound.backend.dto.pagination.SPFResponse;
import com.puresound.backend.entity.jpa.metadata.artist.ArtistMetadata;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface ArtistMapper {
    ArtistResponse toResponse(ArtistMetadata artist);

    List<ArtistResponse> toResponses(List<ArtistMetadata> artists);

    SimplifiedArtistResponse toSimplifiedResponse(ArtistMetadata artist);

    default SPFResponse<SimplifiedArtistResponse> toSpfSimplifiedResponse(Page<ArtistMetadata> artistMetadataPage) {
        List<SimplifiedArtistResponse> content = artistMetadataPage
                .map(this::toSimplifiedResponse)
                .getContent();

        Sort currentSort = artistMetadataPage.getSort();
        String sortBy = currentSort.isSorted() ? currentSort.iterator().next().getProperty() : null;
        String sortDirection = currentSort.isSorted() ? currentSort.iterator().next().getDirection().name() : null;

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(artistMetadataPage.getNumber() + 1)
                .size(artistMetadataPage.getSize())
                .first(artistMetadataPage.isFirst())
                .last(artistMetadataPage.isLast())
                .numberOfElements(artistMetadataPage.getNumberOfElements())
                .totalElements(artistMetadataPage.getTotalElements())
                .totalPages(artistMetadataPage.getTotalPages())
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        return SPFResponse.of(content, pagingResponse);
    }
}
