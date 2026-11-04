
package com.signalmap.liveSignalMap.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.entity.BssidMapping;
import com.signalmap.liveSignalMap.repository.AuditoriumRepository;
import com.signalmap.liveSignalMap.repository.BssidMappingRepository;
import com.signalmap.liveSignalMap.repository.MobileMeasurementRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import com.signalmap.liveSignalMap.entity.MobileMeasurement;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import com.signalmap.liveSignalMap.repository.WifiMeasurementRepository;

@AutoConfigureMockMvc
class BssidControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    private BssidMappingRepository bssidMappingRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private WifiMeasurementRepository wifiMeasurementRepository;

    @Autowired
    private MobileMeasurementRepository mobileMeasurementRepository;

    @AfterEach
    void tearDown() {
        wifiMeasurementRepository.deleteAll();
        mobileMeasurementRepository.deleteAll();
        bssidMappingRepository.deleteAll();
        auditoriumRepository.deleteAll();
    }

    @Test
    void shouldCreateBssidMappingThroughHttp() throws Exception {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("Тестовая аудитория 312");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);

        Auditorium savedAuditorium =
                auditoriumRepository.saveAndFlush(auditorium);

        Long auditoriumId = savedAuditorium.getId();


        String bssid = "10:20:30:40:50:60";

        String requestJson = """
                {
                    "bssid": "%s",
                    "auditoriumId": %d
                }
                """.formatted(bssid, auditoriumId);

        String responseJson = mockMvc.perform(
                        post("/api/bssid")
                                .with(csrf())
                                .with(httpBasic(
                                        "test-admin",
                                        "test-password-123"
                                ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.bssid").value(bssid))
                .andExpect(jsonPath("$.auditoriumId")
                        .value(auditoriumId))
                .andExpect(jsonPath("$.createdAt").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode response = objectMapper.readTree(responseJson);
        Long mappingId = response.get("id").asLong();

        entityManager.clear();

        BssidMapping saved = bssidMappingRepository
                .findById(mappingId)
                .orElseThrow();

        assertEquals(bssid, saved.getBssid());
        assertEquals(auditoriumId, saved.getAuditoriumId());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void shouldRejectInvalidBssidFormat() throws Exception {
        String requestJson = """
                {
                    "bssid": "INVALID-BSSID",
                    "auditoriumId": 1
                }
                """;

        long countBefore = bssidMappingRepository.count();

        mockMvc.perform(
                        post("/api/bssid")
                                .with(csrf())
                                .with(httpBasic(
                                        "test-admin",
                                        "test-password-123"
                                ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.details.bssid")
                        .value("Invalid BSSID format"));

        assertEquals(
                countBefore,
                bssidMappingRepository.count()
        );
    }

    @Test
    void shouldRejectMissingRequiredFields() throws Exception {
        String requestJson = "{}";

        long countBefore = bssidMappingRepository.count();

        mockMvc.perform(
                        post("/api/bssid")
                                .with(csrf())
                                .with(httpBasic(
                                        "test-admin",
                                        "test-password-123"
                                ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.details.bssid")
                        .value("BSSID is required"))
                .andExpect(jsonPath("$.details.auditoriumId")
                        .value("Auditorium ID is required"));

        assertEquals(
                countBefore,
                bssidMappingRepository.count()
        );
    }

    @Test
    void shouldRejectDuplicateBssid() throws Exception {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("Тестовая аудитория 312");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);

        Long auditoriumId = auditoriumRepository
                .saveAndFlush(auditorium)
                .getId();

        String bssid = "AA:BB:CC:DD:EE:FF";

        String requestJson = """
                {
                    "bssid": "%s",
                    "auditoriumId": %d
                }
                """.formatted(bssid, auditoriumId);

        mockMvc.perform(
                        post("/api/bssid")
                                .with(csrf())
                                .with(httpBasic(
                                        "test-admin",
                                        "test-password-123"
                                ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isCreated());

        long countAfterFirstRequest = bssidMappingRepository.count();

        mockMvc.perform(
                        post("/api/bssid")
                                .with(csrf())
                                .with(httpBasic(
                                        "test-admin",
                                        "test-password-123"
                                ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message")
                        .value("BSSID already exists: " + bssid));

        assertEquals(
                countAfterFirstRequest,
                bssidMappingRepository.count()
        );

        BssidMapping saved = bssidMappingRepository
                .findByBssid(bssid)
                .orElseThrow();

        assertEquals(auditoriumId, saved.getAuditoriumId());
    }


    @Test
    void shouldRejectBssidMappingWithNonexistentAuditorium() throws Exception {
        Long nonexistentAuditoriumId = 999999999L;

        assertFalse(
                auditoriumRepository.existsById(nonexistentAuditoriumId)
        );

        String bssid = "22:33:44:55:66:77";

        String requestJson = """
            {
                "bssid": "%s",
                "auditoriumId": %d
            }
            """.formatted(bssid, nonexistentAuditoriumId);

        long countBefore = bssidMappingRepository.count();

        mockMvc.perform(
                        post("/api/bssid")
                                .with(csrf())
                                .with(httpBasic(
                                        "test-admin",
                                        "test-password-123"
                                ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));

        assertEquals(
                countBefore,
                bssidMappingRepository.count()
        );

        assertFalse(
                bssidMappingRepository.existsByBssid(bssid)
        );
    }


    @Test
    void shouldRejectBssidCreationWithoutAuthentication() throws Exception {

        String requestJson = """
            {
                "bssid": "33:44:55:66:77:88",
                "auditoriumId": 1
            }
            """;

        long countBefore = bssidMappingRepository.count();

        mockMvc.perform(
                        post("/api/bssid")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isUnauthorized());

        assertEquals(
                countBefore,
                bssidMappingRepository.count()
        );
    }

    @Test
    void shouldRejectBssidDeletionWithoutAuthentication() throws Exception {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("Тестовая аудитория 315");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);

        Long auditoriumId = auditoriumRepository
                .saveAndFlush(auditorium)
                .getId();


        BssidMapping mapping = new BssidMapping();
        mapping.setBssid("44:55:66:77:88:99");
        mapping.setAuditoriumId(auditoriumId);

        BssidMapping saved = bssidMappingRepository
                .saveAndFlush(mapping);

        Long mappingId = saved.getId();


        mockMvc.perform(
                        delete("/api/bssid/{id}", mappingId)
                                .with(csrf())
                )
                .andExpect(status().isUnauthorized());


        assertTrue(bssidMappingRepository.existsById(mappingId));
    }


    @Test
    void shouldRejectAuditoriumDeletionWhenBssidExists() throws Exception {


        Auditorium auditorium = new Auditorium();
        auditorium.setName("Тестовая аудитория 131");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(1);

        Long auditoriumId = auditoriumRepository
                .saveAndFlush(auditorium)
                .getId();


        BssidMapping mapping = new BssidMapping();
        mapping.setBssid("55:66:77:88:99:AA");
        mapping.setAuditoriumId(auditoriumId);

        Long mappingId = bssidMappingRepository
                .saveAndFlush(mapping)
                .getId();


        mockMvc.perform(
                        delete("/api/auditoriums/{id}", auditoriumId)
                                .with(csrf())
                                .with(httpBasic(
                                        "test-admin",
                                        "test-password-123"
                                ))
                )
                .andExpect(status().isConflict());


        assertTrue(auditoriumRepository.existsById(auditoriumId));
        assertTrue(bssidMappingRepository.existsById(mappingId));
    }

    @Test
    void shouldRejectAuditoriumDeletionWhenWifiMeasurementsExist() throws Exception {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("Тестовая аудитория Wi-Fi");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(1);

        Long auditoriumId = auditoriumRepository
                .saveAndFlush(auditorium)
                .getId();


        WifiMeasurement measurement = WifiMeasurement.builder()
                .bssid("AA:11:BB:22:CC:33")
                .auditoriumId(auditoriumId)
                .downloadSpeed(45.0)
                .uploadSpeed(20.0)
                .ping(18)
                .rssi(-55)
                .timestamp(LocalDateTime.now())
                .build();

        Long measurementId = wifiMeasurementRepository
                .saveAndFlush(measurement)
                .getId();


        mockMvc.perform(delete("/api/auditoriums/{id}", auditoriumId)
                        .with(csrf())
                        .with(httpBasic("test-admin", "test-password-123")))
                .andExpect(status().isConflict());

        assertTrue(auditoriumRepository.existsById(auditoriumId));
        assertTrue(wifiMeasurementRepository.existsById(measurementId));
    }

    @Test
    void shouldRejectAuditoriumDeletionWhenMobileMeasurementsExist() throws Exception {

        // Создаём аудиторию
        Auditorium auditorium = new Auditorium();
        auditorium.setName("Тестовая аудитория Mobile");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(1);

        Long auditoriumId = auditoriumRepository
                .saveAndFlush(auditorium)
                .getId();
        MobileMeasurement measurement = MobileMeasurement.builder()
                .operator("MTS")
                .auditoriumId(auditoriumId)
                .downloadSpeed(65.0)
                .uploadSpeed(25.0)
                .ping(30)
                .timestamp(LocalDateTime.now())
                .build();

        Long measurementId = mobileMeasurementRepository
                .saveAndFlush(measurement)
                .getId();

        mockMvc.perform(delete("/api/auditoriums/{id}", auditoriumId)
                        .with(csrf())
                        .with(httpBasic("test-admin", "test-password-123")))
                .andExpect(status().isConflict());

        assertTrue(auditoriumRepository.existsById(auditoriumId));
        assertTrue(mobileMeasurementRepository.existsById(measurementId));
    }


}
