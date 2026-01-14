package com.puresound.backend.repository.jpa.image;

import com.puresound.backend.constant.image.OwnerType;
import com.puresound.backend.entity.jpa.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findByImageOwnerIdAndImageOwnerType(String imageOwnerId, OwnerType imageOwnerType);

    // Batch fetch images by owner IDs and owner type
    List<Image> findAllByImageOwnerIdInAndImageOwnerType(Collection<String> ownerIds, OwnerType imageOwnerType);
}
