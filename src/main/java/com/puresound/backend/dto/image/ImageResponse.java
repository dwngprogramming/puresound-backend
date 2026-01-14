package com.puresound.backend.dto.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.puresound.backend.constant.image.OwnerType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ImageResponse(
        String id,
        String name,
        OwnerType imageOwnerType,
        String url,
        Integer width,
        Integer height
) {
}
