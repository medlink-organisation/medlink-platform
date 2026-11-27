package medlink.project.notificationservice.controller;

import medlink.project.notificationservice.security.JwtAuthConverter;
import medlink.project.notificationservice.security.SecurityConfig;
import medlink.project.notificationservice.service.NotificationService;
import medlink.project.notificationservice.validation.NotificationRoleValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
@Import({SecurityConfig.class, JwtAuthConverter.class, NotificationRoleValidator.class})
class NotificationControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void rejectsUnauthenticatedUser() throws Exception {
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsUnsupportedRole() throws Exception {
        mockMvc.perform(get("/api/notifications").with(jwt()
                        .jwt(token -> token.subject("user-sub"))
                        .authorities(() -> "ROLE_TECHNICAL")))
                .andExpect(status().isForbidden());
    }

    @Test
    void allowsPatientPraticienAndAdmin() throws Exception {
        when(notificationService.getForUser(eq("user-sub"), any(), eq(false), any()))
                .thenReturn(Page.empty());

        for (String role : new String[]{"ROLE_PATIENT", "ROLE_PRATICIEN", "ROLE_ADMIN"}) {
            mockMvc.perform(get("/api/notifications").with(jwt()
                            .jwt(token -> token.subject("user-sub"))
                            .authorities(() -> role)))
                    .andExpect(status().isOk());
        }
    }
}
