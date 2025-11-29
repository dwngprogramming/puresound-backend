package com.puresound.backend.mapper.metadata;

import com.puresound.backend.dto.metadata.album.AlbumResponse;
import com.puresound.backend.dto.metadata.album.BasicAlbumResponse;
import com.puresound.backend.dto.metadata.artist.ArtistResponse;
import com.puresound.backend.entity.jpa.metadata.album.AlbumMetadata;
import com.puresound.backend.entity.jpa.metadata.artist.ArtistAlbumMetadata;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AlbumDecorator implements AlbumMapper {

    @Autowired
    AlbumMapper delegate;

    @Autowired
    ArtistMapper artistMapper;

    @Override
    public AlbumResponse toAlbumResponse(AlbumMetadata album) {
        // Map các field thông thường
        AlbumResponse response = delegate.toAlbumResponse(album);

        List<ArtistResponse> sortedArtists = album.getArtists()
                .stream()
                .sorted(Comparator.comparingInt(ArtistAlbumMetadata::getArtistOrder))
                .map(ArtistAlbumMetadata::getArtist)
                .map(artistMapper::toResponse)
                .toList();

        return response.withArtists(sortedArtists);       // Create a new AlbumResponse with the artists populated
    }

    @Override
    public BasicAlbumResponse toBasicAlbumResponse(AlbumMetadata album) {
        BasicAlbumResponse response = delegate.toBasicAlbumResponse(album);

        List<ArtistResponse> sortedArtists = album.getArtists()
                .stream()
                .sorted(Comparator.comparingInt(ArtistAlbumMetadata::getArtistOrder))
                .map(ArtistAlbumMetadata::getArtist)
                .map(artistMapper::toResponse)
                .toList();

        return response.withArtists(sortedArtists);
    }
}
