package medlink.project.directoyservice.controller;

import medlink.project.directoyservice.dto.AffectationPraticienDto;
import medlink.project.directoyservice.security.JwtAuthConverter;
import medlink.project.directoyservice.security.SecurityConfig;
import medlink.project.directoyservice.service.AffectationPraticienService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AffectationPraticienController.class)
@Import({SecurityConfig.class, JwtAuthConverter.class})
class BookingCatalogSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AffectationPraticienService affectationService;

    @Test
    void patientReadsOnlyBookingAssignmentView() throws Exception {
        AffectationPraticienDto assignment = AffectationPraticienDto.builder()
                .id(1L).praticienId(2L).cliniqueId(3L).cliniqueNom("Clinique Centre")
                .actif(true).deleted(false).build();
        when(affectationService.getActiveByClinique(eq(3L), any()))
                .thenReturn(new PageImpl<>(List.of(assignment)));

        mockMvc.perform(get("/affectations-praticiens/booking/active/by-clinique/3")
                        .with(authentication(patient())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].praticienId").value(2))
                .andExpect(jsonPath("$.content[0].dateAffectation").doesNotExist());
    }

    @Test
    void unauthenticatedCatalogCallIsRejected() throws Exception {
        mockMvc.perform(get("/affectations-praticiens/booking/active/by-clinique/3"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void patientCannotCreateAssignment() throws Exception {
        mockMvc.perform(post("/affectations-praticiens")
                        .with(authentication(patient()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"praticienId\":2,\"cliniqueId\":3}"))
                .andExpect(status().isForbidden());
    }

    private TestingAuthenticationToken patient() {
        return new TestingAuthenticationToken("patient", "n/a", "ROLE_PATIENT");
    }
}
