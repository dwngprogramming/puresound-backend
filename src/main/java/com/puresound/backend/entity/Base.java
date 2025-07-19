package com.puresound.backend.entity;

import com.github.f4b6a3.ulid.UlidCreator;
import com.puresound.backend.constant.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class Base {

    @Id
    @Column(columnDefinition = "CHAR(26)", nullable = false, updatable = false)
    String id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", nullable = false)
    Status status = Status.ACTIVE;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false, length = 100)
    String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    String updatedBy;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;        // null when not deleted

    @PrePersist
    public void generateUlid() {
        if (this.id == null) {
            this.id = UlidCreator.getUlid().toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Base that = (Base) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
