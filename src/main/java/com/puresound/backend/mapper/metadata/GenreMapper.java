package com.puresound.backend.mapper.metadata;

import com.puresound.backend.dto.metadata.genre.GenreResponse;
import com.puresound.backend.entity.jpa.metadata.genre.GenreMetadata;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface GenreMapper {
    GenreResponse toResponse(GenreMetadata genreMetadata);
}
