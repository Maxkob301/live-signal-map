
package com.signalmap.liveSignalMap.service;

import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.dto.BssidMappingRequest;
import com.signalmap.liveSignalMap.dto.BssidMappingResponse;
import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.exception.BssidNotFoundException;
import com.signalmap.liveSignalMap.repository.AuditoriumRepository;
import com.signalmap.liveSignalMap.repository.BssidMappingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

        BssidMappingRequest request = new BssidMappingRequest();
        request.setBssid("AA:BB:CC:DD:EE:FF");
        request.setAuditoriumId(auditorium.getId());
        BssidMappingResponse saved = bssidService.create(request);

        BssidMappingResponse found =
                bssidService.findByBssid("AA:BB:CC:DD:EE:FF");

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();

        assertThat(found.getBssid())
                .isEqualTo("AA:BB:CC:DD:EE:FF");

        assertThat(found.getAuditoriumId())
                .isEqualTo(auditorium.getId());

        assertThat(found.getId())
                .isEqualTo(saved.getId());
    }

    @Test
    void shouldThrowWhenNotFound() {
        assertThatThrownBy(
                () -> bssidService.findByBssid("FF:FF:FF:FF:FF:FF")
        ).isInstanceOf(BssidNotFoundException.class);
    }

    @Test
    void shouldDelete() {

        BssidMappingRequest request = new BssidMappingRequest();
        request.setBssid("11:22:33:44:55:66");
        request.setAuditoriumId(auditorium.getId());

        BssidMappingResponse saved = bssidService.create(request);


        bssidService.delete(saved.getId());


        assertThat(bssidMappingRepository.findById(saved.getId()))
                .isEmpty();
    }
}
