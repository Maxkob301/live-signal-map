package com.signalmap.liveSignalMap.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wifi_measurements")
public class WifiMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bssid;

    @Column(name = "auditorium_id")
    private Long auditoriumId;

    @Column(name = "download_speed")
    private Double downloadSpeed;

    @Column(name = "upload_speed")
    private Double uploadSpeed;

    private Integer ping;

    private Integer rssi;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "device_hash")
    private String deviceHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}