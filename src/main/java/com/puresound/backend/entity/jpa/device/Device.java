package com.puresound.backend.entity.jpa.device;

import com.puresound.backend.constant.device.DeviceType;
import com.puresound.backend.entity.jpa.Base;
import com.puresound.backend.entity.jpa.listener.Listener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Device extends Base {

    @Column(nullable = false)
    String ip;

    @Column(nullable = false)
    String os;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_category", nullable = false)
    DeviceType deviceType;

    @Column(name = "last_login")
    LocalDateTime lastLogin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listener_id", nullable = false)
    Listener listener;
}
