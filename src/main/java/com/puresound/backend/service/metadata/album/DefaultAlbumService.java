package com.puresound.backend.service.metadata.album;

import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.pagination.SPFRequest;
import com.puresound.backend.dto.pagination.SPFResponse;
import com.puresound.backend.entity.jpa.metadata.album.AlbumMetadata;
import com.puresound.backend.mapper.metadata.AlbumMapper;
import com.puresound.backend.repository.jpa.metadata.album.AlbumRepository;
import com.puresound.backend.service.image.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class DefaultAlbumService implements AlbumService {
    AlbumRepository albumRepository;
    AlbumMapper albumMapper;
    ImageService imageService;

    @Override
    public SPFResponse<SimplifiedAlbumResponse> getPopularAlbums(SPFRequest request) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(), request.sort());
        Page<AlbumMetadata> albumMetadataPage = albumRepository.findAll(pageable);
        SPFResponse<SimplifiedAlbumResponse> dataConvert = albumMapper.toSpfSimplifiedResponses(albumMetadataPage);

        List<SimplifiedAlbumResponse> albums = dataConvert.content();
        List<SimplifiedAlbumResponse> albumsAfterAddImages = albums.stream()
                .map(imageService::addImagesToSimplifiedAlbum)
                .toList();
        return SPFResponse.of(albumsAfterAddImages, dataConvert.paging());
    }
}
