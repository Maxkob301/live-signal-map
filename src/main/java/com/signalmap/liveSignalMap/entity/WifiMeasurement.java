package com.signalmap.liveSignalMap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "wifi_measurements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WifiMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bssid;

    @Column(name = "auditorium_id")
    private Long auditoriumId;

    @Column(name = "download_speed", nullable = false)
    private Double downloadSpeed;

    @Column(name = "upload_speed", nullable = false)
    private Double uploadSpeed;

    @Column(nullable = false)
    private Integer ping;

    private Integer rssi;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "device_hash")
    private String deviceHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}