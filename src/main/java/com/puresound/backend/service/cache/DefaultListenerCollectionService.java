package com.puresound.backend.service.cache;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.entity.redis.listener_collection.ListenerCollectionCache;
import com.puresound.backend.exception.exts.ConflictException;
import com.puresound.backend.repository.redis.ListenerCollectionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class DefaultListenerCollectionService implements ListenerCollectionService {
    static int MAX_RECENTLY_TRACKS = 25;
    ListenerCollectionRepository collectionRepository;

    @Override
    public ListenerCollectionCache getById(String listenerId) {
        return collectionRepository
                .getByListenerId(listenerId)
                .orElse(null);
    }

    @Override
    public void create(ListenerCollectionCache collection) {
        if (isExists(collection.getListenerId()))
            throw new ConflictException(ApiMessage.LISTENER_COLLECTION_EXISTS, LogLevel.INFO);
        collectionRepository.save(collection);
    }

    @Override
    public boolean isExists(String listenerId) {
        return collectionRepository.existsById(listenerId);
    }

    @Override
    public ListenerCollectionCache update(ListenerCollectionCache collection) {
        ListenerCollectionCache currentCollection = getById(collection.getListenerId());

        // All fields in instance collection are always not null (because of validation)
        // Handle for recently track list size limit
        List<String> collectRecentlyTrackIds = collection.getPlaybackHistory().getRecentlyTrackIds();
        String lastTrackId = collection.getPlaybackHistory().getLastTrackId();

        // Client sends empty recently track ids list, keep the existing list
        if (currentCollection != null) {
            if (collectRecentlyTrackIds.isEmpty()) {
                collection.getPlaybackHistory().setRecentlyTrackIds(
                        currentCollection.getPlaybackHistory().getRecentlyTrackIds()
                );
            }

            if (lastTrackId.isBlank() && !currentCollection.getPlaybackHistory().getLastTrackId().isBlank()) {
                collection.getPlaybackHistory().setLastTrackId(
                        currentCollection.getPlaybackHistory().getLastTrackId()
                );
            }
        } else if (collectRecentlyTrackIds.size() > MAX_RECENTLY_TRACKS) {
            // Client sends recently track ids list exceeding the limit, trim it
            // Lấy một "view" từ vị trí MAX đến cuối và xóa tất cả phần tử trong đó.
            // Thao tác này sửa đổi trực tiếp danh sách `collectRecentlyTrackIds`.
            collectRecentlyTrackIds.subList(MAX_RECENTLY_TRACKS, collectRecentlyTrackIds.size()).clear();
        }

        // Set recently track ids back to collection
        collection.getPlaybackHistory().setRecentlyTrackIds(collectRecentlyTrackIds);
        return collectionRepository.save(collection);
    }
}
