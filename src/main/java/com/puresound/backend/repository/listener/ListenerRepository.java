package com.puresound.backend.repository.listener;

import com.puresound.backend.repository.entity.user.listener.Listener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ListenerRepository extends JpaRepository<Listener, String> {
    @Query("""
            SELECT l
            FROM Listener l
            WHERE (l.username = :usernameOrEmail OR l.email = :usernameOrEmail) AND l.status = 'ACTIVE'
            """)
    Optional<Listener> loginByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    @Query("""
            SELECT COUNT(l) > 0
            FROM Listener l
            WHERE (l.username = :usernameOrEmail OR l.email = :usernameOrEmail) AND l.status = 'LOCKED'
            """)
    boolean isLockedAccount(String usernameOrEmail);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Listener> findByEmail(String email);

    @Modifying
    @Query("""
            UPDATE Listener l
            SET l.password = :encodedPassword, l.updatedBy = :email, l.updatedAt = CURRENT_TIMESTAMP
            WHERE l.email = :email
            """)
    void resetPassword(String email, String encodedPassword);

    @Modifying
    @Query("""
            UPDATE Listener l
            SET l.lastLoginAt = CURRENT_TIMESTAMP
            WHERE l.id = :id
            """)
    void updateLastLoginAt(String id);
}
