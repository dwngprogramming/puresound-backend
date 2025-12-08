package com.puresound.backend.mapper.metadata;

import com.puresound.backend.dto.metadata.album.AlbumResponse;
import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.entity.jpa.metadata.album.AlbumMetadata;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class, uses = {ArtistMapper.class, TrackMapper.class})
@DecoratedWith(AlbumDecorator.class)
public interface AlbumMapper {

    @Mapping(target = "artists", ignore = true)
    AlbumResponse toAlbumResponse(AlbumMetadata album);

    @Mapping(target = "artists", ignore = true)
    SimplifiedAlbumResponse toSimplifiedAlbumResponse(AlbumMetadata album);
}
