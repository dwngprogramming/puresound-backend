package com.puresound.backend.repository.jpa.metadata.album;

import com.puresound.backend.entity.jpa.metadata.album.AlbumMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumMetadata, String> {
    Page<AlbumMetadata> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
