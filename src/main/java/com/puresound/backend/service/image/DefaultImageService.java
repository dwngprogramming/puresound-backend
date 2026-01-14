package com.puresound.backend.service.image;

import com.puresound.backend.constant.image.OwnerType;
import com.puresound.backend.dto.image.ImageResponse;
import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.metadata.artist.ArtistResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.entity.jpa.image.Image;
import com.puresound.backend.mapper.image.ImageMapper;
import com.puresound.backend.repository.jpa.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class DefaultImageService implements ImageService {
    ImageRepository imageRepository;
    ImageMapper imageMapper;

    @Override
    public Map<String, List<ImageResponse>> getImagesByOwnerIdsAndOwnerType(Collection<String> ownerIds, OwnerType ownerType) {
        List<Image> images = imageRepository.findAllByImageOwnerIdInAndImageOwnerType(ownerIds, ownerType);
        return images.stream()
                .collect(Collectors.groupingBy(
                        Image::getImageOwnerId,
                        Collectors.mapping(imageMapper::toResponse, Collectors.toList())
                ));
    }

    @Override
    public ArtistResponse addImagesToArtist(ArtistResponse artist) {
        List<Image> image = imageRepository.findByImageOwnerIdAndImageOwnerType(artist.id(), OwnerType.ARTIST);

        return artist.toBuilder()
                .images(imageMapper.toResponses(image))
                .build();
    }

    @Override
    public List<ArtistResponse> addImagesToArtists(List<ArtistResponse> artists) {
        List<String> ids = artists.stream().map(ArtistResponse::id).toList();
        Map<String, List<ImageResponse>> imagesMap = getImagesByOwnerIdsAndOwnerType(ids, OwnerType.ARTIST);

        return artists.stream()
                .map(a -> a.toBuilder()
                        .images(imagesMap.getOrDefault(a.id(), List.of()))
                        .build())
                .toList();
    }

    @Override
    public SimplifiedArtistResponse addImagesToSimplifiedArtist(SimplifiedArtistResponse artist) {
        List<Image> images = imageRepository.findByImageOwnerIdAndImageOwnerType(artist.id(), OwnerType.ARTIST);

        return artist.toBuilder()
                .images(imageMapper.toResponses(images))
                .build();
    }

    @Override
    public List<SimplifiedArtistResponse> addImagesToSimplifiedArtists(List<SimplifiedArtistResponse> artists) {
        List<String> ids = artists.stream().map(SimplifiedArtistResponse::id).toList();
        Map<String, List<ImageResponse>> imagesMap = getImagesByOwnerIdsAndOwnerType(ids, OwnerType.ARTIST);

        return artists.stream()
                .map(a -> a.toBuilder()
                        .images(imagesMap.getOrDefault(a.id(), List.of()))
                        .build())
                .toList();
    }

    @Override
    public SimplifiedAlbumResponse addImagesToSimplifiedAlbum(SimplifiedAlbumResponse album) {
        List<Image> images = imageRepository.findByImageOwnerIdAndImageOwnerType(album.id(), OwnerType.ALBUM);

        return album.toBuilder()
                .images(imageMapper.toResponses(images))
                .build();
    }

    @Override
    public List<SimplifiedAlbumResponse> addImagesToSimplifiedAlbums(List<SimplifiedAlbumResponse> albums) {
        List<String> ids = albums.stream().map(SimplifiedAlbumResponse::id).toList();
        Map<String, List<ImageResponse>> imagesMap = getImagesByOwnerIdsAndOwnerType(ids, OwnerType.ALBUM);

        return albums.stream()
                .map(a -> a.toBuilder()
                        .images(imagesMap.getOrDefault(a.id(), List.of()))
                        .build())
                .toList();
    }
}
