package com.puresound.backend.entity.metadata.artist;

import com.puresound.backend.entity.Base;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artist_metadata")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArtistMetadata extends Base {

    @Column(name = "stage_name", nullable = false)
    String stageName;

    @Column(name = "bio", columnDefinition = "TEXT")
    String bio;

    @Column(name = "country")
    String country;

    @Column(name = "locale")
    String locale;

    @Column(name = "followers", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Builder.Default
    Long followers = 0L;

    @Column(name = "popularity", nullable = false)
    @Builder.Default
    Integer popularity = 0;

    @Column(name = "verified", nullable = false)
    @Builder.Default
    Boolean verified = false;

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ArtistAlbumMetadata> albums = new ArrayList<>();

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ArtistTrackMetadata> tracks = new ArrayList<>();
}
