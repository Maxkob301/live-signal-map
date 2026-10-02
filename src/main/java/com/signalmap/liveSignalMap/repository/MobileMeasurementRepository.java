package com.signalmap.liveSignalMap.repository;


import com.signalmap.liveSignalMap.dto.MobileHeatmapDto;
import com.signalmap.liveSignalMap.entity.MobileMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobileMeasurementRepository extends JpaRepository<MobileMeasurement, Long> {

}
