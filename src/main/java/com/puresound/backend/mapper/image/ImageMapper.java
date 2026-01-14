package com.puresound.backend.mapper.image;

import com.puresound.backend.dto.image.ImageResponse;
import com.puresound.backend.entity.jpa.image.Image;
import com.puresound.backend.mapper.GlobalMapperConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface ImageMapper {
    ImageResponse toResponse(Image image);

    List<ImageResponse> toResponses(List<Image> images);
}
