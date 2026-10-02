package com.signalmap.liveSignalMap.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mobile_measurements")
public class MobileMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operator;

    @Column(name = "download_speed")
    private Double downloadSpeed;

    @Column(name = "upload_speed")
    private Double uploadSpeed;

    private Integer ping;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "device_hash")
    private String deviceHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}