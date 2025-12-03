package com.puresound.backend.mapper.metadata;

import com.puresound.backend.constant.metadata.Bitrate;
import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.metadata.artist.ArtistResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.dto.metadata.track.SimplifiedTrackResponse;
import com.puresound.backend.dto.metadata.track.TrackResponse;
import com.puresound.backend.entity.jpa.metadata.artist.ArtistTrackMetadata;
import com.puresound.backend.entity.jpa.metadata.track.TrackMetadata;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Comparator;
import java.util.List;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class TrackDecorator implements TrackMapper {

    @Autowired
    TrackMapper delegate;

    @Autowired
    ArtistMapper artistMapper;

    @Lazy
    @Autowired
    AlbumMapper albumMapper;

    @Override
    public TrackResponse toResponse(TrackMetadata trackMetadata) {
        TrackResponse response = delegate.toResponse(trackMetadata);
        List<ArtistResponse> artists = trackMetadata.getArtists().stream()
                .sorted(Comparator.comparingInt(ArtistTrackMetadata::getArtistOrder))
                .map(ArtistTrackMetadata::getArtist)
                .map(artistMapper::toResponse)
                .toList();

        SimplifiedAlbumResponse album = albumMapper.toSimplifiedAlbumResponse(trackMetadata.getAlbum());

        List<Integer> availableBitrates = trackMetadata.getAvailableBitrates()
                .stream()
                .map(Bitrate::getBitrate)
                .toList();

        return response.withAdditionalData(artists, album, availableBitrates);
    }

    @Override
    public SimplifiedTrackResponse toSimplifiedResponse(TrackMetadata trackMetadata) {
        SimplifiedTrackResponse response = delegate.toSimplifiedResponse(trackMetadata);

        SimplifiedAlbumResponse album = albumMapper.toSimplifiedAlbumResponse(trackMetadata.getAlbum());

        List<SimplifiedArtistResponse> artists = trackMetadata.getArtists().stream()
                .sorted(Comparator.comparingInt(ArtistTrackMetadata::getArtistOrder))
                .map(ArtistTrackMetadata::getArtist)
                .map(artistMapper::toSimplifiedResponse)
                .toList();
        return response.withArtistsAndAlbum(artists, album);
    }
}
