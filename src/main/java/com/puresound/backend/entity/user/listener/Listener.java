package com.puresound.backend.entity.user.listener;

import com.puresound.backend.constant.user.Gender;
import com.puresound.backend.constant.user.OAuth2Provider;
import com.puresound.backend.entity.Base;
import com.puresound.backend.entity.user.device.Device;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

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

    @Column(nullable = false, length = 150)
    String password;

    @Column
    String firstname;

    @Column
    String lastname;

    @Column(length = 20)
    String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Gender gender;

    @Column
    LocalDate dob;

    @Column
    String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth2")
    OAuth2Provider oauth2;

    @OneToMany(mappedBy = "listener", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Device> devices;
}
