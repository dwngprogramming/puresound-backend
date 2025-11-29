package com.puresound.backend.entity.jpa.metadata.album;

import com.puresound.backend.constant.metadata.AlbumType;
import com.puresound.backend.constant.metadata.DatePrecision;
import com.puresound.backend.entity.jpa.Base;
import com.puresound.backend.entity.jpa.metadata.artist.ArtistAlbumMetadata;
import com.puresound.backend.entity.jpa.metadata.track.TrackMetadata;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "album_metadata")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AlbumMetadata extends Base {

    // UPC chuẩn cần có 12 ký tự, nhưng Spotify đôi khi trả về 14???
    @Column(name = "upc", unique = true, columnDefinition = "VARCHAR(16)")
    String upc;

    @Column(name = "ean", unique = true, columnDefinition = "CHAR(13)")
    String ean;

    @Column(nullable = false)
    String name;

    @Column(name = "album_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    AlbumType albumType;

    @Column(name = "total_tracks", nullable = false)
    @Builder.Default
    Integer totalTracks = 1;

    @Column(name = "total_duration_ms", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Builder.Default
    Long totalDurationMs = 0L;

    @Column(name = "release_date", nullable = false)
    LocalDate releaseDate;

    @Column(name = "release_tz", length = 10)
    String releaseTz;

    @Column(name = "release_date_precision", nullable = false)
    @Enumerated(value = EnumType.STRING)
    DatePrecision releaseDatePrecision;

    @Column(name = "popularity", nullable = false)
    @Builder.Default
    Integer popularity = 0;

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ArtistAlbumMetadata> artists = new ArrayList<>();

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<TrackMetadata> tracks = new ArrayList<>();
}
