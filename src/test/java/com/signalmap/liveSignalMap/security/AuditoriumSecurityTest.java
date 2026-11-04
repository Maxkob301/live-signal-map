
package com.signalmap.liveSignalMap.security;

import com.signalmap.liveSignalMap.controller.AuditoriumController;
import com.signalmap.liveSignalMap.service.AuditoriumService;
import com.signalmap.liveSignalMap.config.SecurityConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
@WebMvcTest(AuditoriumController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "app.security.admin.username=test-admin",
        "app.security.admin.password=$2b$10$LSopOgtkN72WU.LY7.rWYeMn6b1OVm41lrJRDhQI4vMUCKMHJ2YnW"
})
class AuditoriumSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuditoriumService auditoriumService;

    @Test
    void anonymousUserCannotDeleteAuditorium() throws Exception {
        mockMvc.perform(delete("/api/auditoriums/1"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(auditoriumService);
    }

    @Test
    void adminCanDeleteAuditorium() throws Exception {
        mockMvc.perform(delete("/api/auditoriums/1")
                        .with(httpBasic("test-admin", "test-password-123")))
                .andExpect(status().isNoContent());

        verify(auditoriumService).delete(1L);
    }
}
