package com.signalmap.liveSignalMap.service;

import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.dto.MobileMeasurementRequest;
import com.signalmap.liveSignalMap.dto.MobileMeasurementResponse;
import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.repository.AuditoriumRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MobileMeasurementServiceTest extends AbstractIntegrationTest {

    @Autowired
    private MobileMeasurementService mobileMeasurementService;

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @Test
    void shouldSaveMeasurementAndSetTimestamp() {
        // Arrange: создаём аудиторию, чтобы внешний ключ сработал
        Auditorium auditorium = new Auditorium();
        auditorium.setName("Test Auditorium");
        auditorium.setBuilding("Main");
        Auditorium savedAuditorium = auditoriumRepository.save(auditorium);

        MobileMeasurementRequest request = new MobileMeasurementRequest();
        request.setOperator("MTS");
        request.setAuditoriumId(savedAuditorium.getId());  // ← реальный ID
        request.setDownloadSpeed(25.5);
        request.setUploadSpeed(5.5);
        request.setPing(30);
        request.setDeviceHash("test-hash-mobile");

        // Act
        MobileMeasurementResponse response = mobileMeasurementService.save(request);

        // Assert
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOperator()).isEqualTo("MTS");
        assertThat(response.getAuditoriumId()).isEqualTo(savedAuditorium.getId());
        assertThat(response.getDownloadSpeed()).isEqualTo(25.5);
        assertThat(response.getPing()).isEqualTo(30);
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void shouldSaveMeasurementWithoutAuditorium() {
        // Arrange
        MobileMeasurementRequest request = new MobileMeasurementRequest();
        request.setOperator("Beeline");
        request.setDownloadSpeed(10.0);
        request.setUploadSpeed(2.0);
        request.setPing(50);
        // auditoriumId не задаём — null

        // Act
        MobileMeasurementResponse response = mobileMeasurementService.save(request);

        // Assert
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOperator()).isEqualTo("Beeline");
        assertThat(response.getAuditoriumId()).isNull();
        assertThat(response.getTimestamp()).isNotNull();
    }
}