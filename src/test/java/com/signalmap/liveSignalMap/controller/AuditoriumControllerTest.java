package com.signalmap.liveSignalMap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.repository.AuditoriumRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "app.security.admin.username=test-admin",
        "app.security.admin.password=$2b$10$LSopOgtkN72WU.LY7.rWYeMn6b1OVm41lrJRDhQI4vMUCKMHJ2YnW"
})
class AuditoriumControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @Test
    void shouldCreateAuditorium() throws Exception {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("312");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);

        mockMvc.perform(post("/api/auditoriums")
                        .with(httpBasic("test-admin", "test-password-123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditorium)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("312"))
                .andExpect(jsonPath("$.building").value("Корпус А"))
                .andExpect(jsonPath("$.floor").value(3));
    }

    @Test
    void shouldGetAllAuditoriums() throws Exception {
        mockMvc.perform(get("/api/auditoriums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldUpdateAuditorium() throws Exception {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("312");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);

        Auditorium saved = auditoriumRepository.save(auditorium);

        Auditorium updatedData = new Auditorium();
        updatedData.setName("313");
        updatedData.setBuilding("Корпус Б");
        updatedData.setFloor(4);

        mockMvc.perform(put("/api/auditoriums/{id}", saved.getId())
                        .with(httpBasic("test-admin", "test-password-123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("313"))
                .andExpect(jsonPath("$.building").value("Корпус Б"))
                .andExpect(jsonPath("$.floor").value(4));
    }

    @Test
    void shouldGetAuditoriumById() throws Exception {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("312");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);

        Auditorium saved = auditoriumRepository.save(auditorium);

        mockMvc.perform(get("/api/auditoriums/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("312"))
                .andExpect(jsonPath("$.building").value("Корпус А"))
                .andExpect(jsonPath("$.floor").value(3));
    }

    @Test
    void shouldReturnNotFoundWhenAuditoriumDoesNotExist() throws Exception {
        long nonexistentId = Long.MAX_VALUE;

        mockMvc.perform(get("/api/auditoriums/{id}", nonexistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAuditorium() throws Exception {

        Auditorium auditorium = new Auditorium();
        auditorium.setName("312");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);

        Auditorium saved = auditoriumRepository.save(auditorium);
        Long auditoriumId = saved.getId();


        mockMvc.perform(delete("/api/auditoriums/{id}", auditoriumId)
                        .with(httpBasic("test-admin", "test-password-123")))
                .andExpect(status().isNoContent());


        assertFalse(auditoriumRepository.existsById(auditoriumId));
    }

    @Test
    void shouldRejectDeleteWithoutAuthentication() throws Exception {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("313");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);

        Auditorium saved = auditoriumRepository.save(auditorium);
        Long auditoriumId = saved.getId();


        mockMvc.perform(delete("/api/auditoriums/{id}", auditoriumId))
                .andExpect(status().isUnauthorized());


        assertTrue(auditoriumRepository.existsById(auditoriumId));
    }

    @Test
    void shouldRejectCreateWithoutAuthentication() throws Exception {
        String requestBody = """
            {
                "name": "314",
                "building": "Корпус А",
                "floor": 3
            }
            """;

        mockMvc.perform(post("/api/auditoriums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectUpdateWithoutAuthentication() throws Exception {

        Auditorium auditorium = new Auditorium();
        auditorium.setName("315");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);

        Auditorium saved = auditoriumRepository.save(auditorium);
        Long auditoriumId = saved.getId();


        String requestBody = """
            {
                "name": "999",
                "building": "Корпус Б",
                "floor": 5
            }
            """;


        mockMvc.perform(put("/api/auditoriums/{id}", auditoriumId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());


        Auditorium unchanged = auditoriumRepository.findById(auditoriumId)
                .orElseThrow();

        assertEquals("315", unchanged.getName());
        assertEquals("Корпус А", unchanged.getBuilding());
        assertEquals(3, unchanged.getFloor());
    }

    @Test
    void shouldReturnErrorResponseWhenAuditoriumNotFound() throws Exception {
        long nonexistentId = Long.MAX_VALUE;

        mockMvc.perform(get("/api/auditoriums/{id}", nonexistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.details").doesNotExist());
    }
}