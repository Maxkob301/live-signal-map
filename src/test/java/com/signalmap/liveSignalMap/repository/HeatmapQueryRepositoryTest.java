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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


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
    void shouldReturnEmptyWhenNoData(){
        List<WifiHeatmapDto> result = heatmapRepository.getWifiHeatmap(from, to);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldAggregateWifiByAuditorium() {
        Auditorium auditorium = createAuditorium("312", "Корпус А");
        createWifi(auditorium.getId(), 50.0, 10.0, 15, -60);
        createWifi(auditorium.getId(), 60.0, 12.0, 10, -55);

        List<WifiHeatmapDto> result = heatmapRepository.getWifiHeatmap(from, to);

        assertThat(result).hasSize(1);
        WifiHeatmapDto dto = result.getFirst();
        assertThat(dto.getAuditoriumId()).isEqualTo(auditorium.getId());
        assertThat(dto.getAuditoriumName()).isEqualTo("312");
        assertThat(dto.getBuilding()).isEqualTo("Корпус А");
        assertThat(dto.getAvgDownloadSpeed()).isEqualTo(55.0);
        assertThat(dto.getAvgUploadSpeed()).isEqualTo(11.0);
        assertThat(dto.getAvgPing()).isEqualTo(12.5);
        assertThat(dto.getMeasurementCount()).isEqualTo(2);
    }

    @Test
    void shouldAggregateMobileByOperator() {
        createMobile("MTS", 25.0, 5.0, 30);
        createMobile("MTS", 30.0, 6.0, 25);
        createMobile("Beeline", 20.0, 4.0, 35);

        List<MobileHeatmapDto> result = heatmapRepository.getMobileHeatmap(from, to);

        assertThat(result).hasSize(2);

        MobileHeatmapDto mts = result.stream()
                .filter(d -> d.getOperator().equals("MTS"))
                .findFirst().orElseThrow();
        assertThat(mts.getAvgDownloadSpeed()).isEqualTo(27.5);
        assertThat(mts.getMeasurementCount()).isEqualTo(2);

        MobileHeatmapDto beeline = result.stream()
                .filter(d -> d.getOperator().equals("Beeline"))
                .findFirst().orElseThrow();
        assertThat(beeline.getMeasurementCount()).isEqualTo(1);
    }

    @Test
    void shouldFilterByTime() {
        Auditorium auditorium = createAuditorium("312", "Корпус А");

        WifiMeasurement old = createWifi(auditorium.getId(), 50.0, 10.0, 15, -60);
        old.setTimestamp(LocalDateTime.of(2025, 1, 1, 0, 0));
        wifiMeasurementRepository.save(old);

        createWifi(auditorium.getId(), 60.0, 12.0, 10, -55);

        List<WifiHeatmapDto> result = heatmapRepository.getWifiHeatmap(from, to);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getMeasurementCount()).isEqualTo(1);
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
        WifiMeasurement m = new WifiMeasurement();
        m.setBssid("AA:BB:CC:DD:EE:FF");
        m.setAuditoriumId(auditoriumId);
        m.setDownloadSpeed(download);
        m.setUploadSpeed(upload);
        m.setPing(ping);
        m.setRssi(rssi);
        m.setTimestamp(LocalDateTime.now());
        m.setDeviceHash("test-hash");
        m.setCreatedAt(LocalDateTime.now());
        return wifiMeasurementRepository.save(m);
    }

    private MobileMeasurement createMobile(String operator, Double download,
                                           Double upload, Integer ping) {
        MobileMeasurement m = new MobileMeasurement();
        m.setOperator(operator);
        m.setDownloadSpeed(download);
        m.setUploadSpeed(upload);
        m.setPing(ping);
        m.setTimestamp(LocalDateTime.now());
        m.setDeviceHash("test-hash");
        m.setCreatedAt(LocalDateTime.now());
        return mobileMeasurementRepository.save(m);
    }
}
