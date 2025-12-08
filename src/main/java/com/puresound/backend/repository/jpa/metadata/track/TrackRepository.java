package com.puresound.backend.repository.jpa.metadata.track;

import com.puresound.backend.entity.jpa.metadata.track.TrackMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends PagingAndSortingRepository<TrackMetadata, String>, JpaRepository<TrackMetadata, String> {
}
