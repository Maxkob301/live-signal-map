package com.signalmap.liveSignalMap.service;

import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.exception.AuditoriumNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@TestPropertySource(properties = {
        "app.security.admin.username=test-admin",
        "app.security.admin.password=$2b$10$LSopOgtkN72WU.LY7.rWYeMn6b1OVm41lrJRDhQI4vMUCKMHJ2YnW"
})
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

    @Test
    void shouldDeleteAuditorium() {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("Тестовая аудитория");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);

        Auditorium saved = auditoriumService.create(auditorium);
        Long auditoriumId = saved.getId();


        auditoriumService.delete(auditoriumId);

        assertThatThrownBy(() -> auditoriumService.findById(auditoriumId))
                .isInstanceOf(AuditoriumNotFoundException.class);
    }

    @Test
    void shouldUpdateAuditorium() {

        Auditorium auditorium = new Auditorium();
        auditorium.setName("312");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);

        Auditorium saved = auditoriumService.create(auditorium);
        Long auditoriumId = saved.getId();


        Auditorium updatedData = new Auditorium();
        updatedData.setName("313");
        updatedData.setBuilding("Корпус Б");
        updatedData.setFloor(4);


        Auditorium updated = auditoriumService.update(
                auditoriumId,
                updatedData
        );


        assertThat(updated.getId()).isEqualTo(auditoriumId);
        assertThat(updated.getName()).isEqualTo("313");
        assertThat(updated.getBuilding()).isEqualTo("Корпус Б");
        assertThat(updated.getFloor()).isEqualTo(4);


        Auditorium fromDatabase = auditoriumService.findById(auditoriumId);

        assertThat(fromDatabase.getName()).isEqualTo("313");
        assertThat(fromDatabase.getBuilding()).isEqualTo("Корпус Б");
        assertThat(fromDatabase.getFloor()).isEqualTo(4);
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentAuditorium() {
        Auditorium updatedData = new Auditorium();
        updatedData.setName("313");
        updatedData.setBuilding("Корпус Б");
        updatedData.setFloor(4);

        assertThatThrownBy(() ->
                auditoriumService.update(999999L, updatedData)
        ).isInstanceOf(AuditoriumNotFoundException.class);
    }
}