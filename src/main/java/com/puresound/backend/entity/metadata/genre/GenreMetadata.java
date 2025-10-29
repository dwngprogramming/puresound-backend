package com.puresound.backend.entity.metadata.genre;

import com.puresound.backend.entity.Base;
import com.puresound.backend.entity.metadata.track.TrackMetadata;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genre_metadata")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenreMetadata extends Base {

    @Column(nullable = false, unique = true, length = 100)
    String name;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    @Builder.Default
    Set<TrackMetadata> tracks = new HashSet<>();
}
