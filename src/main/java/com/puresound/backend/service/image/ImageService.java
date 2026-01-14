package com.puresound.backend.service.image;

import com.puresound.backend.constant.image.OwnerType;
import com.puresound.backend.dto.image.ImageResponse;
import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.metadata.artist.ArtistResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ImageService {

    Map<String, List<ImageResponse>> getImagesByOwnerIdsAndOwnerType(Collection<String> ownerIds, OwnerType ownerType);

    ArtistResponse addImagesToArtist(ArtistResponse artist);

    List<ArtistResponse> addImagesToArtists(List<ArtistResponse> artist);

    SimplifiedArtistResponse addImagesToSimplifiedArtist(SimplifiedArtistResponse artist);

    List<SimplifiedArtistResponse> addImagesToSimplifiedArtists(List<SimplifiedArtistResponse> artists);

    SimplifiedAlbumResponse addImagesToSimplifiedAlbum(SimplifiedAlbumResponse album);

    List<SimplifiedAlbumResponse> addImagesToSimplifiedAlbums(List<SimplifiedAlbumResponse> albums);
}
