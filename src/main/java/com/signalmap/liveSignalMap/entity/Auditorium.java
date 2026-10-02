package com.signalmap.liveSignalMap.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name = "auditoriums")
public class Auditorium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String building;

    private Integer floor;
}