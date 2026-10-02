package com.signalmap.liveSignalMap.service;

import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.entity.BssidMapping;
import com.signalmap.liveSignalMap.exception.BssidNotFoundException;
import com.signalmap.liveSignalMap.repository.AuditoriumRepository;
import com.signalmap.liveSignalMap.repository.BssidMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BssidServiceTest extends AbstractIntegrationTest {

    @Autowired
    private BssidService bssidService;

    @Autowired
    private BssidMappingRepository bssidMappingRepository;

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    private Auditorium auditorium;

    @BeforeEach
    void setUp() {
        bssidMappingRepository.deleteAll();
        auditoriumRepository.deleteAll();

        auditorium = new Auditorium();
        auditorium.setName("312");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);
        auditorium = auditoriumRepository.save(auditorium);
    }

    @Test
    void shouldCreateAndFindByBssid() {
        BssidMapping mapping = new BssidMapping();
        mapping.setBssid("AA:BB:CC:DD:EE:FF");
        mapping.setAuditoriumId(auditorium.getId());
        mapping.setCreatedAt(LocalDateTime.now());

        BssidMapping saved = bssidService.create(mapping);
        BssidMapping found = bssidService.findByBssid("AA:BB:CC:DD:EE:FF");

        assertThat(saved.getId()).isNotNull();
        assertThat(found.getBssid()).isEqualTo("AA:BB:CC:DD:EE:FF");
        assertThat(found.getAuditoriumId()).isEqualTo(auditorium.getId());
    }

    @Test
    void shouldThrowWhenNotFound() {
        assertThatThrownBy(() -> bssidService.findByBssid("FF:FF:FF:FF:FF:FF"))
                .isInstanceOf(BssidNotFoundException.class);
    }

    @Test
    void shouldDelete() {
        BssidMapping mapping = new BssidMapping();
        mapping.setBssid("11:22:33:44:55:66");
        mapping.setAuditoriumId(auditorium.getId());
        mapping.setCreatedAt(LocalDateTime.now());
        BssidMapping saved = bssidService.create(mapping);

        bssidService.delete(saved.getId());

        assertThat(bssidMappingRepository.findById(saved.getId())).isEmpty();
    }
}