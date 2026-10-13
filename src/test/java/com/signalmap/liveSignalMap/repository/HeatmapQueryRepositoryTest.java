package com.signalmap.liveSignalMap.repository;

import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.dto.MobileHeatmapDto;
import com.signalmap.liveSignalMap.dto.WifiHeatmapDto;
import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.entity.MobileMeasurement;
import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HeatmapQueryRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private HeatmapQueryRepository heatmapRepository;

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    private WifiMeasurementRepository wifiMeasurementRepository;

    @Autowired
    private MobileMeasurementRepository mobileMeasurementRepository;

    private LocalDateTime from;
    private LocalDateTime to;

    @BeforeEach
    void setUp() {
        wifiMeasurementRepository.deleteAll();
        mobileMeasurementRepository.deleteAll();
        auditoriumRepository.deleteAll();

        from = LocalDateTime.of(2026, 1, 1, 0, 0);
        to = LocalDateTime.of(2026, 12, 31, 23, 59);
    }

    @Test
    void shouldReturnEmptyWhenNoData() {
        List<WifiHeatmapDto> result = heatmapRepository.getWifiHeatmap(from, to);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldAggregateWifiByAuditorium() {
        Auditorium auditorium = createAuditorium("312", "Корпус А");
        createWifi(auditorium.getId(), 50.0, 10.0, 15, -60);
        createWifi(auditorium.getId(), 60.0, 12.0, 10, -55);
        createWifi(auditorium.getId(), 55.0, 11.0, 12, -58);

        List<WifiHeatmapDto> result = heatmapRepository.getWifiHeatmap(from, to);

        assertThat(result).hasSize(1);
        WifiHeatmapDto dto = result.getFirst();
        assertThat(dto.getAuditoriumId()).isEqualTo(auditorium.getId());
        assertThat(dto.getAuditoriumName()).isEqualTo("312");
        assertThat(dto.getBuilding()).isEqualTo("Корпус А");
        assertThat(dto.getAvgDownloadSpeed()).isEqualTo(55.0);
        assertThat(dto.getAvgUploadSpeed()).isEqualTo(11.0);
        assertThat(dto.getMeasurementCount()).isEqualTo(3);
    }

    @Test
    void shouldNotShowAuditoriumsWithLessThanThreeMeasurements() {
        Auditorium auditorium = createAuditorium("312", "Корпус А");
        createWifi(auditorium.getId(), 50.0, 10.0, 15, -60);
        createWifi(auditorium.getId(), 60.0, 12.0, 10, -55);

        List<WifiHeatmapDto> result = heatmapRepository.getWifiHeatmap(from, to);

        assertThat(result).isEmpty();  // только 2 замера — не показываем
    }

    @Test
    void shouldFilterOutZeroAndUnrealisticValues() {
        Auditorium auditorium = createAuditorium("312", "Корпус А");
        createWifi(auditorium.getId(), 50.0, 10.0, 15, -60);
        createWifi(auditorium.getId(), 60.0, 12.0, 10, -55);
        createWifi(auditorium.getId(), 55.0, 11.0, 12, -58);
        createWifi(auditorium.getId(), 0.0, 0.0, 0, -60);       // мусор
        createWifi(auditorium.getId(), 99999.0, 0.0, 0, -60);   // мусор

        List<WifiHeatmapDto> result = heatmapRepository.getWifiHeatmap(from, to);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getMeasurementCount()).isEqualTo(3);  // только валидные
    }

    @Test
    void shouldAggregateMobileByAuditoriumAndOperator() {
        Auditorium auditorium = createAuditorium("312", "Корпус А");

        createMobile(auditorium.getId(), "MTS", 25.0, 5.0, 30);
        createMobile(auditorium.getId(), "MTS", 30.0, 6.0, 25);
        createMobile(auditorium.getId(), "MTS", 35.0, 7.0, 20);
        createMobile(auditorium.getId(), "Beeline", 20.0, 4.0, 35);
        createMobile(auditorium.getId(), "Beeline", 22.0, 5.0, 33);
        createMobile(auditorium.getId(), "Beeline", 24.0, 6.0, 31);

        List<MobileHeatmapDto> result = heatmapRepository.getMobileHeatmap(from, to);

        assertThat(result).hasSize(2);  // MTS + Beeline в одной аудитории

        MobileHeatmapDto mts = result.stream()
                .filter(d -> d.getOperator().equals("MTS"))
                .findFirst().orElseThrow();
        assertThat(mts.getAuditoriumName()).isEqualTo("312");
        assertThat(mts.getBuilding()).isEqualTo("Корпус А");
        assertThat(mts.getAvgDownloadSpeed()).isEqualTo(30.0);
        assertThat(mts.getMeasurementCount()).isEqualTo(3);

        MobileHeatmapDto beeline = result.stream()
                .filter(d -> d.getOperator().equals("Beeline"))
                .findFirst().orElseThrow();
        assertThat(beeline.getMeasurementCount()).isEqualTo(3);
    }

    @Test
    void shouldFilterByTime() {
        Auditorium auditorium = createAuditorium("312", "Корпус А");

        WifiMeasurement old = createWifi(auditorium.getId(), 50.0, 10.0, 15, -60);
        old.setTimestamp(LocalDateTime.of(2025, 1, 1, 0, 0));
        wifiMeasurementRepository.save(old);

        createWifi(auditorium.getId(), 60.0, 12.0, 10, -55);
        createWifi(auditorium.getId(), 55.0, 11.0, 12, -58);
        createWifi(auditorium.getId(), 58.0, 11.0, 13, -57);

        List<WifiHeatmapDto> result = heatmapRepository.getWifiHeatmap(from, to);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getMeasurementCount()).isEqualTo(3);  // old не в счёт
    }

    private Auditorium createAuditorium(String name, String building) {
        Auditorium auditorium = new Auditorium();
        auditorium.setName(name);
        auditorium.setBuilding(building);
        auditorium.setFloor(3);
        return auditoriumRepository.save(auditorium);
    }

    private WifiMeasurement createWifi(Long auditoriumId, Double download,
                                       Double upload, Integer ping, Integer rssi) {
        WifiMeasurement m = WifiMeasurement.builder()
                .bssid("AA:BB:CC:DD:EE:FF")
                .auditoriumId(auditoriumId)
                .downloadSpeed(download)
                .uploadSpeed(upload)
                .ping(ping)
                .rssi(rssi)
                .timestamp(LocalDateTime.now())
                .deviceHash("test-hash")
                .build();
        return wifiMeasurementRepository.save(m);
    }

    private MobileMeasurement createMobile(Long auditoriumId, String operator,
                                           Double download, Double upload, Integer ping) {
        MobileMeasurement m = MobileMeasurement.builder()
                .operator(operator)
                .auditoriumId(auditoriumId)
                .downloadSpeed(download)
                .uploadSpeed(upload)
                .ping(ping)
                .timestamp(LocalDateTime.now())
                .deviceHash("test-hash")
                .build();
        return mobileMeasurementRepository.save(m);
    }
}