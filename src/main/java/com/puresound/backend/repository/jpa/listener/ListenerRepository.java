package com.puresound.backend.repository.jpa.listener;

import com.puresound.backend.entity.user.listener.Listener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ListenerRepository extends JpaRepository<Listener, String> {
    @Query("SELECT l FROM Listener l WHERE l.username = :usernameOrEmail OR l.email = :usernameOrEmail")
    Optional<Listener> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Listener> findByEmail(String email);
}
