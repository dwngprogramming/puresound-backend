package com.puresound.backend.repository.metadata.album;

import com.puresound.backend.entity.metadata.album.AlbumMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumMetadata, String> {
}
