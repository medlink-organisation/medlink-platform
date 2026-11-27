package medlink.project.medicalrecordservice.audit;

import medlink.project.medicalrecordservice.controller.AuditController;
import medlink.project.medicalrecordservice.security.JwtAuthConverter;
import medlink.project.medicalrecordservice.security.SecurityConfig;
import medlink.project.medicalrecordservice.service.AuditService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

@WebMvcTest(
        controllers = AuditController.class,
        properties = "spring.application.name=medical-record-service"
)
@Import({SecurityConfig.class, JwtAuthConverter.class})
class AuditControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuditService auditService;

    @Test
    void rejectsUnauthenticatedCall() throws Exception {
        mockMvc.perform(get("/api/admin/audit/entities"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "AUDIT_READ")
    void rejectsTechnicalAuditPermissionWithoutMedicalPermission() throws Exception {
        mockMvc.perform(get("/api/admin/audit/entities"))
                .andExpect(status().isForbidden());
    }

    @Test
    void allowsDedicatedMedicalAuditPermissions() throws Exception {
        when(auditService.getAuditedEntityTypes()).thenReturn(List.of("dossier-medical"));

        mockMvc.perform(get("/api/admin/audit/entities").with(authentication(
                        new TestingAuthenticationToken(
                                "medical-auditor", "n/a", "AUDIT_READ", "MEDICAL_AUDIT_READ"
                        )
                )))
                .andExpect(status().isOk());
    }
}
