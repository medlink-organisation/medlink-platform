package medlink.project.workflowservice.controller;

import medlink.project.workflowservice.security.JwtAuthConverter;
import medlink.project.workflowservice.security.SecurityConfig;
import medlink.project.workflowservice.service.PatientBookingService;
import medlink.project.workflowservice.service.PatientIdentityService;
import medlink.project.workflowservice.service.WorkflowService.WorkflowStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PatientBookingController.class)
@Import({SecurityConfig.class, JwtAuthConverter.class})
class PatientBookingControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientIdentityService patientIdentityService;

    @MockitoBean
    private PatientBookingService patientBookingService;

    @Test
    void rejectsUnauthenticatedBooking() throws Exception {
        mockMvc.perform(post("/api/patient/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void patientIdFromBrowserIsIgnoredAndServerIdentityIsUsed() throws Exception {
        when(patientIdentityService.requireActivePatientId(any())).thenReturn(42L);
        when(patientBookingService.book(eq(42L), eq(7L), eq("Consultation"),
                eq("kc-patient"), eq("request-1")))
                .thenReturn(new WorkflowStatus("process-1", true, "attendance", "rdv-1"));

        mockMvc.perform(post("/api/patient/appointments")
                        .with(jwt().jwt(token -> token.subject("kc-patient"))
                                .authorities(() -> "ROLE_PATIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"slotId":7,"reason":"Consultation","idempotencyKey":"request-1","patientId":999}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rendezVousId").value("rdv-1"));

        verify(patientBookingService).book(
                42L, 7L, "Consultation", "kc-patient", "request-1");
    }

    @Test
    void rejectsAdminOnPatientOnlyBookingRoute() throws Exception {
        mockMvc.perform(post("/api/patient/appointments")
                        .with(jwt().authorities(() -> "ROLE_ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson()))
                .andExpect(status().isForbidden());
    }

    private String requestJson() {
        return """
                {"slotId":7,"reason":"Consultation","idempotencyKey":"request-1"}
                """;
    }
}
