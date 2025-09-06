package com.puresound.backend.repository.entity.user.oauth2;

import com.github.f4b6a3.ulid.UlidCreator;
import com.puresound.backend.constant.user.OAuth2Type;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "oauth2_provider")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OAuth2Provider {
    @Id
    @Column(columnDefinition = "CHAR(26)", nullable = false, updatable = false)
    String id;

    @Column(name = "user_id", nullable = false, updatable = false)
    String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    OAuth2Type provider;

    @Column(name = "linked_at", nullable = false)
    LocalDateTime linkedAt;

    @Column(name = "linked", nullable = false)
    boolean linked;

    @Column(name = "unlinked_at")
    LocalDateTime unlinkedAt;

    @PrePersist
    public void generateUlid() {
        if (this.id == null) {
            this.id = UlidCreator.getUlid().toString();
        }
    }
}
