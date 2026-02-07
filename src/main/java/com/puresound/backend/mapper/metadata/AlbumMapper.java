package com.puresound.backend.mapper.metadata;

import com.puresound.backend.dto.metadata.album.AlbumResponse;
import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.pagination.PagingResponse;
import com.puresound.backend.dto.pagination.SPFResponse;
import com.puresound.backend.entity.jpa.metadata.album.AlbumMetadata;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class, uses = {ArtistMapper.class, TrackMapper.class})
@DecoratedWith(AlbumDecorator.class)
public interface AlbumMapper {

    @Mapping(target = "artists", ignore = true)
    AlbumResponse toAlbumResponse(AlbumMetadata album);

    @Mapping(target = "artists", ignore = true)
    SimplifiedAlbumResponse toSimplifiedAlbumResponse(AlbumMetadata album);

    default SPFResponse<SimplifiedAlbumResponse> toSpfSimplifiedResponses(Page<AlbumMetadata> albumMetadataPage) {
        List<SimplifiedAlbumResponse> content = albumMetadataPage
                .map(this::toSimplifiedAlbumResponse)
                .getContent();

        Sort currentSort = albumMetadataPage.getSort();
        String sortBy = currentSort.isSorted() ? currentSort.iterator().next().getProperty() : null;
        String sortDirection = currentSort.isSorted() ? currentSort.iterator().next().getDirection().name() : null;

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(albumMetadataPage.getNumber() + 1)
                .size(albumMetadataPage.getSize())
                .first(albumMetadataPage.isFirst())
                .last(albumMetadataPage.isLast())
                .numberOfElements(albumMetadataPage.getNumberOfElements())
                .totalElements(albumMetadataPage.getTotalElements())
                .totalPages(albumMetadataPage.getTotalPages())
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        return SPFResponse.of(content, pagingResponse);
    }
}
