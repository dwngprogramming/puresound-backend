package com.puresound.backend.repository.metadata.track;

import com.puresound.backend.entity.metadata.track.TrackMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends JpaRepository<TrackMetadata, String> {
}
