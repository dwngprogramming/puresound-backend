package com.puresound.backend.repository.jpa.metadata.genre;

import com.puresound.backend.entity.jpa.metadata.genre.GenreMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreMetadata, String> {
}
