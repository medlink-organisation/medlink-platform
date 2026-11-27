package medlink.project.praticienservice.controller;

import medlink.project.praticienservice.dto.PraticienDto;
import medlink.project.praticienservice.dto.BookingPraticienDto;
import medlink.project.praticienservice.repository.SpecialiteRepository;
import medlink.project.praticienservice.security.JwtAuthConverter;
import medlink.project.praticienservice.security.SecurityConfig;
import medlink.project.praticienservice.service.PraticienService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PraticienController.class)
@Import({SecurityConfig.class, JwtAuthConverter.class})
class PraticienControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PraticienService praticienService;

    @MockitoBean
    private SpecialiteRepository specialiteRepository;

    @Test
    void allowsAdminToUpdatePractitioner() throws Exception {
        PraticienDto updated = PraticienDto.builder()
                .id(7L)
                .nom("Ben Salah")
                .prenom("Amira")
                .email("amira@example.com")
                .phone("+21612345678")
                .numeroOrdre("ORD-7")
                .specialiteId(2L)
                .actif(true)
                .build();
        when(praticienService.update(eq(7L), any(PraticienDto.class))).thenReturn(updated);

        mockMvc.perform(patch("/praticiens/update/7")
                        .with(authentication(new TestingAuthenticationToken(
                                "admin", "n/a", "ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nom": "Ben Salah",
                                  "prenom": "Amira",
                                  "email": "amira@example.com",
                                  "phone": "+21612345678",
                                  "numeroOrdre": "ORD-7",
                                  "specialiteId": 2
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.nom").value("Ben Salah"));
    }

    @Test
    void rejectsPractitionerUpdatingAnotherPractitionerById() throws Exception {
        mockMvc.perform(patch("/praticiens/update/7")
                        .with(authentication(new TestingAuthenticationToken(
                                "practitioner", "n/a", "ROLE_PRATICIEN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nom": "Ben Salah"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void allowsPatientToReadOnlyActiveBookingCatalog() throws Exception {
        when(praticienService.getActiveForBooking(eq(2L), any()))
                .thenReturn(new PageImpl<>(java.util.List.of(
                        new BookingPraticienDto(7L, "Ben Salah", "Amira", 2L))));

        mockMvc.perform(get("/praticiens/booking/active/by-specialite/2")
                        .with(authentication(new TestingAuthenticationToken(
                                "patient", "n/a", "ROLE_PATIENT"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(7))
                .andExpect(jsonPath("$.content[0].email").doesNotExist())
                .andExpect(jsonPath("$.content[0].keycloakId").doesNotExist());
    }

    @Test
    void rejectsPatientAdministrativePractitionerUpdate() throws Exception {
        mockMvc.perform(patch("/praticiens/update/7")
                        .with(authentication(new TestingAuthenticationToken(
                                "patient", "n/a", "ROLE_PATIENT")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Tentative\"}"))
                .andExpect(status().isForbidden());
    }
}
