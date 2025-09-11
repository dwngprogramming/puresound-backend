package com.puresound.backend.repository.subscription.listener;

import com.puresound.backend.entity.subscription.listener.ListenerSubPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ListenerSubPeriodRepository extends JpaRepository<ListenerSubPeriod, String> {
    @Query("""
            SELECT lsp FROM ListenerSubPeriod lsp
            LEFT JOIN FETCH lsp.subscription ls
            LEFT JOIN FETCH ls.listener l
            LEFT JOIN FETCH ls.plan p
            WHERE l.id = :listenerId
            AND lsp.startDate <= CURRENT_TIMESTAMP
            AND lsp.endDate >= CURRENT_TIMESTAMP
            ORDER BY lsp.startDate DESC
            """)
    Optional<ListenerSubPeriod> findCurrentByListenerId(@Param("listenerId") String listenerId);
}
