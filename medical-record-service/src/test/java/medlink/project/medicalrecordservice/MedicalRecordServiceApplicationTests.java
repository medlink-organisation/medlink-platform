package medlink.project.medicalrecordservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@SpringBootTest
@AutoConfigureMockMvc
class MedicalRecordServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void rejectsAnonymousMedicalDocumentDownload() throws Exception {
        mockMvc.perform(get(
                        "/medical-records/exam-results/{id}/download",
                        UUID.randomUUID()
                ))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsWorkflowInternalApiWithoutServiceRole() throws Exception {
        mockMvc.perform(get("/internal/medical-records/patients/{patientId}", 42L).with(jwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    void acceptsWorkflowServiceRoleOnInternalApi() throws Exception {
        mockMvc.perform(get("/internal/medical-records/patients/{patientId}", 42L)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_WORKFLOW_SERVICE")
                        )))
                .andExpect(status().isNotFound());
    }

}
