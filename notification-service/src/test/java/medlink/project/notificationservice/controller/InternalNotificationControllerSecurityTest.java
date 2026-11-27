package medlink.project.notificationservice.controller;

import medlink.project.notificationservice.dto.EmailNotificationResponse;
import medlink.project.notificationservice.security.JwtAuthConverter;
import medlink.project.notificationservice.security.SecurityConfig;
import medlink.project.notificationservice.service.EmailService;
import medlink.project.notificationservice.service.NotificationService;
import medlink.project.notificationservice.validation.NotificationProducerValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InternalNotificationController.class)
@Import({
        SecurityConfig.class,
        JwtAuthConverter.class,
        NotificationProducerValidator.class
})
class InternalNotificationControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private EmailService emailService;

    @Test
    void rejectsUnauthenticatedRequest() throws Exception {
        mockMvc.perform(post("/internal/notifications/email")
                        .contentType("application/json")
                        .content(validRequest()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsTokenFromUntrustedClientEvenWithAdminRole() throws Exception {
        mockMvc.perform(post("/internal/notifications/email")
                        .with(jwt()
                                .jwt(token -> token.claim("azp", "medlink-web"))
                                .authorities(() -> "ROLE_ADMIN"))
                        .contentType("application/json")
                        .content(validRequest()))
                .andExpect(status().isForbidden());
    }

    @Test
    void acceptsTrustedClientWithoutWorkflowRole() throws Exception {
        UUID notificationId = UUID.randomUUID();
        when(emailService.send(any())).thenReturn(
                new EmailNotificationResponse(
                        notificationId, "SENT", Instant.parse("2026-11-22T20:00:00Z"), "wf-1"
                )
        );

        mockMvc.perform(post("/internal/notifications/email")
                        .with(jwt()
                                .jwt(token -> token.claim("azp", "workflow-service"))
                                .authorities())
                        .contentType("application/json")
                        .content(validRequest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationId").value(notificationId.toString()))
                .andExpect(jsonPath("$.status").value("SENT"));
    }

    private String validRequest() {
        return """
                {
                  "to": "patient@example.com",
                  "subject": "Confirmation",
                  "body": "Votre rendez-vous est confirmé.",
                  "correlationId": "wf-1"
                }
                """;
    }
}
