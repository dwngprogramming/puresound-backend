package com.puresound.backend.entity.user.listener;

import com.puresound.backend.constant.user.Gender;
import com.puresound.backend.entity.Base;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "listeners")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Listener extends Base {

    @Column(unique = true)
    String username;

    @Column(nullable = false, unique = true)
    String email;

    @Column(length = 150)
    String password;

    @Column
    String firstname;

    @Column
    String lastname;

    @Column(length = 20)
    String phone;

    @Enumerated(EnumType.STRING)
    @Column
    Gender gender;

    @Column
    LocalDate dob;

    @Column
    String avatar;

    @Column(name = "last_login_at", columnDefinition = "DATETIME(6)")
    Instant lastLoginAt;
}
