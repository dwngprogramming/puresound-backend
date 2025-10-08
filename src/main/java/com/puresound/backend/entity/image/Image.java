package com.puresound.backend.entity.image;

import com.puresound.backend.constant.image.OwnerType;
import com.puresound.backend.entity.Base;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "images")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image extends Base {

    @Column(columnDefinition = "CHAR(26)", nullable = false)
    String imageOwnerId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "image_owner_type", nullable = false)
    OwnerType imageOwnerType;

    @Column(nullable = false, length = 1024)
    String url;

    @Column(nullable = false)
    Integer width;

    @Column(nullable = false)
    Integer height;

    @Column(nullable = false)
    Long size;
}
