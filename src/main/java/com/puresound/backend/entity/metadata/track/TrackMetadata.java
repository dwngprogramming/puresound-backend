package com.puresound.backend.entity.metadata.track;

import com.puresound.backend.constant.metadata.Bitrate;
import com.puresound.backend.entity.Base;
import com.puresound.backend.entity.metadata.album.AlbumMetadata;
import com.puresound.backend.entity.metadata.artist.ArtistTrackMetadata;
import com.puresound.backend.entity.metadata.genre.GenreMetadata;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "track_metadata")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrackMetadata extends Base {

    @Column(name = "isrc", nullable = false, unique = true, columnDefinition = "CHAR(12)")
    String isrc;

    @Column(nullable = false)
    String title;

    @Column(name = "duration_ms", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    Long durationMs;

    @Column(name = "explicit", nullable = false)
    Boolean explicit;

    @Column(name = "track_number", nullable = false)
    Integer trackNumber;

    @Column(name = "popularity", nullable = false)
    @Builder.Default
    Integer popularity = 0;

    @Column(name = "is_local", nullable = false)
    @Builder.Default
    Boolean isLocal = false;

    @ElementCollection
    @CollectionTable(
            name = "track_bitrate_metadata",
            joinColumns = @JoinColumn(name = "track_id")
    )
    @Convert(converter = BitrateConverter.class)
    @Column(name = "bitrate")
    List<Bitrate> availableBitrates = new ArrayList<>();

    @OneToMany(mappedBy = "track", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ArtistTrackMetadata> artists = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    AlbumMetadata album;

    @ManyToMany
    @JoinTable(
            name = "track_genre_metadata",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @Builder.Default
    Set<GenreMetadata> genres = new HashSet<>();
}
