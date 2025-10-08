package com.puresound.backend.entity.metadata.artist;

import com.puresound.backend.entity.Base;
import com.puresound.backend.entity.metadata.track.TrackMetadata;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "artist_track_metadata",
        uniqueConstraints = @UniqueConstraint(columnNames = {"artist_id", "track_id", "artist_order"})
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArtistTrackMetadata extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    ArtistMetadata artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    TrackMetadata track;

    @Column(name = "artist_order", nullable = false)
    Integer artistOrder;

    @Column(name = "is_main_artist", nullable = false)
    @Builder.Default
    Boolean isMainArtist = false;
}
