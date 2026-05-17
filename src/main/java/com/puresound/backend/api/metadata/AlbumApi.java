package com.puresound.backend.api.metadata;

import com.github.f4b6a3.ulid.Ulid;
import com.puresound.backend.api_docs.metadata.AlbumDocs;
import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.metadata.album.AlbumResponse;
import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.pagination.SPFRequest;
import com.puresound.backend.dto.pagination.SPFResponse;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.service.metadata.album.AlbumService;
import com.puresound.backend.util.ApiResponseFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/albums")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AlbumApi implements AlbumDocs {
    AlbumService albumService;
    ApiResponseFactory apiResponseFactory;

    @NonFinal
    @Value("${paging-size.default}")
    Integer defaultSize;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ApiResponse<AlbumResponse>> getAlbumById(@PathVariable String id, Locale locale) {
        if (!Ulid.isValid(id)) throw new BadRequestException(ApiMessage.ALBUM_ID_INVALID, LogLevel.INFO);
        AlbumResponse response = albumService.getAlbumById(id);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.GET_ALBUM_SUCCESS, response, locale));
    }

    @GetMapping(value = "/popular-albums", produces = "application/json")
    public ResponseEntity<ApiResponse<SPFResponse<SimplifiedAlbumResponse>>> getPopularAlbums(@RequestParam(defaultValue = "1") Integer page,
                                                                                                    Locale locale) {
        Integer sanitizedPage = Math.max(page, 1);
        SPFRequest request = SPFRequest.withPopularityDefaultSort(sanitizedPage, defaultSize);
        SPFResponse<SimplifiedAlbumResponse> response = albumService.getPopularAlbums(request);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.GET_ALBUMS_SUCCESS, response, locale));
    }
}
