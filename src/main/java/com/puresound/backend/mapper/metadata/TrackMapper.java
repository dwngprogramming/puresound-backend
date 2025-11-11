package com.puresound.backend.mapper.metadata;

import com.puresound.backend.dto.metadata.track.TrackResponse;
import com.puresound.backend.entity.jpa.metadata.track.TrackMetadata;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class, uses = {ArtistMapper.class, AlbumMapper.class, GenreMapper.class})
@DecoratedWith(TrackDecorator.class)
public interface TrackMapper {

    @Mapping(target = "album", ignore = true)
    @Mapping(target = "artists", ignore = true)
    @Mapping(target = "availableBitrates", ignore = true)
    TrackResponse toResponse(TrackMetadata trackMetadata);
}
