package com.puresound.backend.repository.metadata.genre;

import com.puresound.backend.entity.metadata.genre.GenreMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreMetadata, String> {
}
