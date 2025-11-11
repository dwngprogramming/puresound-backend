package com.puresound.backend.repository.jpa.metadata.artist;

import com.puresound.backend.entity.jpa.metadata.artist.ArtistMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistMetadata, String> {
}
