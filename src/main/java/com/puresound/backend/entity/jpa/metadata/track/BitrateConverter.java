package com.puresound.backend.entity.jpa.metadata.track;

import com.puresound.backend.constant.metadata.Bitrate;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

@Converter(autoApply = true)
public class BitrateConverter implements AttributeConverter<Bitrate, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Bitrate bitrate) {
        return bitrate == null ? null : bitrate.getBitrate();
    }

    @Override
    public Bitrate convertToEntityAttribute(Integer dbData) {
        if (dbData == null) return null;

        return Arrays.stream(Bitrate.values())
                .filter(b -> b.getBitrate() == dbData)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid bitrate: " + dbData));
    }
}
