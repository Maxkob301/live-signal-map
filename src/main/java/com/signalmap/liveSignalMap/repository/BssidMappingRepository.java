package com.signalmap.liveSignalMap.repository;


import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.entity.BssidMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BssidMappingRepository extends JpaRepository<BssidMapping, Long> {

    Optional<BssidMapping> findByBssid(String bssid);
}
