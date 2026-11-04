
package com.signalmap.liveSignalMap.controller;

import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.entity.MobileMeasurement;
import com.signalmap.liveSignalMap.repository.MobileMeasurementRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.repository.AuditoriumRepository;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
class MobileMeasurementIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MobileMeasurementRepository mobileMeasurementRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AuditoriumRepository auditoriumRepository;


    @Test
    void shouldSaveMobileMeasurementThroughHttp() throws Exception {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("Тестовая аудитория 131");
        auditorium.setBuilding("Стромынка, 20");
        auditorium.setFloor(1);

        Auditorium savedAuditorium = auditoriumRepository.saveAndFlush(auditorium);
        Long auditoriumId = savedAuditorium.getId();

        assertNotNull(auditoriumId);


        String requestJson = """
            {
                "operator": "MTS",
                "downloadSpeed": 25.5,
                "uploadSpeed": 5.5,
                "ping": 30,
                "auditoriumId": %d,
                "deviceHash": "integration-test-device"
            }
            """.formatted(auditoriumId);

        // Act: отправляем HTTP-запрос
        String responseJson = mockMvc.perform(
                        post("/api/measurements/mobile")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.operator").value("MTS"))
                .andExpect(jsonPath("$.auditoriumId").value(auditoriumId))
                .andExpect(jsonPath("$.downloadSpeed").value(25.5))
                .andExpect(jsonPath("$.ping").value(30))
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode response = objectMapper.readTree(responseJson);

        Long measurementId = response.get("id").asLong();


        entityManager.clear();


        MobileMeasurement saved = mobileMeasurementRepository
                .findById(measurementId)
                .orElseThrow();

        assertEquals("MTS", saved.getOperator());
        assertEquals(auditoriumId, saved.getAuditoriumId());
        assertEquals(25.5, saved.getDownloadSpeed());
        assertEquals(5.5, saved.getUploadSpeed());
        assertEquals(30, saved.getPing());
        assertEquals("integration-test-device", saved.getDeviceHash());

        assertNotNull(saved.getTimestamp());
        assertNotNull(saved.getCreatedAt());


        assertTrue(auditoriumRepository.existsById(auditoriumId));
    }


    @Test
    void shouldSaveMobileMeasurementWithoutAuditorium() throws Exception {
        String requestJson = """
            {
                "operator": "Beeline",
                "downloadSpeed": 42.5,
                "uploadSpeed": 12.0,
                "ping": 25,
                "deviceHash": "test-device-without-room"
            }
            """;


        String responseJson = mockMvc.perform(
                        post("/api/measurements/mobile")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.operator").value("Beeline"))
                .andExpect(jsonPath("$.auditoriumId").value(
                        org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.downloadSpeed").value(42.5))
                .andExpect(jsonPath("$.ping").value(25))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode response = objectMapper.readTree(responseJson);
        Long measurementId = response.get("id").asLong();


        entityManager.clear();

        MobileMeasurement saved = mobileMeasurementRepository
                .findById(measurementId)
                .orElseThrow();

        assertNull(saved.getAuditoriumId());
        assertEquals("Beeline", saved.getOperator());
        assertEquals(42.5, saved.getDownloadSpeed());
        assertEquals(12.0, saved.getUploadSpeed());
        assertEquals(25, saved.getPing());
        assertEquals("test-device-without-room", saved.getDeviceHash());

        assertNotNull(saved.getTimestamp());
        assertNotNull(saved.getCreatedAt());
    }


}
