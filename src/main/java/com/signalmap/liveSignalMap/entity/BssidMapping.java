package com.signalmap.liveSignalMap.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bssid_mapping")
public class BssidMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bssid;

    @Column(name = "auditorium_id", nullable = false)
    private Long auditoriumId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}