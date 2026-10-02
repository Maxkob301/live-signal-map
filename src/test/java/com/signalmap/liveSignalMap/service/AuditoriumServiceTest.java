package com.signalmap.liveSignalMap.service;

import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.exception.AuditoriumNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuditoriumServiceTest extends AbstractIntegrationTest {

    @Autowired
    private AuditoriumService auditoriumService;

    @Test
    void shouldCreateAndFindAuditorium() {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("312");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);

        Auditorium saved = auditoriumService.create(auditorium);

        assertThat(saved.getId()).isNotNull();
        assertThat(auditoriumService.findById(saved.getId()).getName()).isEqualTo("312");
    }

    @Test
    void shouldThrowWhenNotFound() {
        assertThatThrownBy(() -> auditoriumService.findById(999L))
                .isInstanceOf(AuditoriumNotFoundException.class);
    }
}