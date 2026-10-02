package com.signalmap.liveSignalMap.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.signalmap.liveSignalMap.dto.MobileHeatmapDto;
import com.signalmap.liveSignalMap.dto.WifiHeatmapDto;
import com.signalmap.liveSignalMap.entity.QAuditorium;
import com.signalmap.liveSignalMap.entity.QMobileMeasurement;
import com.signalmap.liveSignalMap.entity.QWifiMeasurement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HeatmapQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<WifiHeatmapDto> getWifiHeatmap(LocalDateTime from, LocalDateTime to) {
        QWifiMeasurement w = QWifiMeasurement.wifiMeasurement;
        QAuditorium a = QAuditorium.auditorium;

        return queryFactory
                .select(Projections.constructor(WifiHeatmapDto.class,
                        w.auditoriumId,
                        a.name,
                        a.building,
                        w.downloadSpeed.avg(),
                        w.uploadSpeed.avg(),
                        w.ping.avg(),
                        w.rssi.avg(),
                        w.count()
                ))
                .from(w)
                .leftJoin(a).on(a.id.eq(w.auditoriumId))
                .where(
                        w.auditoriumId.isNotNull(),
                        w.timestamp.between(from, to)
                )
                .groupBy(w.auditoriumId, a.name, a.building)
                .fetch();
    }

    public List<MobileHeatmapDto> getMobileHeatmap(LocalDateTime from, LocalDateTime to) {
        QMobileMeasurement m = QMobileMeasurement.mobileMeasurement;

        return queryFactory
                .select(Projections.constructor(MobileHeatmapDto.class,
                        m.operator,
                        m.downloadSpeed.avg(),
                        m.uploadSpeed.avg(),
                        m.ping.avg(),
                        m.count()
                ))
                .from(m)
                .where(
                        m.operator.isNotNull(),
                        m.timestamp.between(from, to)
                )
                .groupBy(m.operator)
                .fetch();
    }
}