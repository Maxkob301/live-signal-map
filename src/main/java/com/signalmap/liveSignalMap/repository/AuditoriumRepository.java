package com.signalmap.liveSignalMap.repository;

import com.signalmap.liveSignalMap.entity.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {
}