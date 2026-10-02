package com.signalmap.liveSignalMap.repository;


import com.signalmap.liveSignalMap.dto.WifiHeatmapDto;
import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WifiMeasurementRepository extends JpaRepository<WifiMeasurement, Long> {

}
