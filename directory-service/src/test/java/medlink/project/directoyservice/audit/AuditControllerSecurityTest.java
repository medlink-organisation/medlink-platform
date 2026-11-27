package medlink.project.directoyservice.audit;

import medlink.project.directoyservice.controller.AuditController;
import medlink.project.directoyservice.security.JwtAuthConverter;
import medlink.project.directoyservice.security.SecurityConfig;
import medlink.project.directoyservice.service.AuditService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuditController.class)
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
    void rejectsAuthenticatedUserWithoutAuditPermission() throws Exception {
        mockMvc.perform(get("/api/admin/audit/entities").with(authentication(
                        new TestingAuthenticationToken("admin", "n/a", "ROLE_ADMIN")
                )))
                .andExpect(status().isForbidden());
    }

    @Test
    void allowsAuditReadPermission() throws Exception {
        when(auditService.getAuditedEntityTypes()).thenReturn(List.of("clinique", "bloc"));

        mockMvc.perform(get("/api/admin/audit/entities").with(authentication(
                        new TestingAuthenticationToken("auditor", "n/a", "AUDIT_READ")
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("clinique"))
                .andExpect(jsonPath("$[1]").value("bloc"));
    }
}
