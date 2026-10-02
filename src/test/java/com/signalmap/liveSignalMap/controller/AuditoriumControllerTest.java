package com.signalmap.liveSignalMap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.entity.Auditorium;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AuditoriumControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAuditorium() throws Exception {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("312");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);

        mockMvc.perform(post("/api/auditoriums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditorium)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("312"))
                .andExpect(jsonPath("$.building").value("Корпус А"));
    }

    @Test
    void shouldGetAllAuditoriums() throws Exception {
        mockMvc.perform(get("/api/auditoriums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}