package com.puresound.backend.repository.subscription.listener;

import com.puresound.backend.entity.subscription.listener.ListenerSubPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ListenerPlanRepository extends JpaRepository<ListenerSubPlan, String> {
    @Query("""
            SELECT p FROM ListenerSubPlan p
            WHERE p.eligibility = 'NEW_ONLY' OR p.eligibility = 'ALL'
            """)
    List<ListenerSubPlan> findForNeverSubscribed();

    @Query("""
            SELECT p FROM ListenerSubPlan p
            WHERE p.eligibility = 'ALL'
            """)
    List<ListenerSubPlan> findForCommonSubscribed();
}
