package com.puresound.backend.repository.redis;

import com.puresound.backend.entity.redis.listener_collection.ListenerCollectionCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListenerCollectionRepository extends CrudRepository<ListenerCollectionCache, String> {
}
