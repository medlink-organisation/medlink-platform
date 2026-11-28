package medlink.project.schedulingservice.controller;

import medlink.project.schedulingservice.dto.CreneauDto;
import medlink.project.schedulingservice.dto.PatientRendezVousDto;
import medlink.project.schedulingservice.entities.StatusRendezVous;
import medlink.project.schedulingservice.entities.StatutCreneau;
import medlink.project.schedulingservice.entities.TypeRendezVous;
import medlink.project.schedulingservice.security.JwtAuthConverter;
import medlink.project.schedulingservice.security.SecurityConfig;
import medlink.project.schedulingservice.service.CreneauCalendarQueryService;
import medlink.project.schedulingservice.service.CreneauService;
import medlink.project.schedulingservice.service.PraticienIdentityService;
import medlink.project.schedulingservice.service.PatientIdentityService;
import medlink.project.schedulingservice.service.RendezVousService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {CreneauController.class, RendezVousController.class},
        properties = "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:9999/jwks")
@Import({SecurityConfig.class, JwtAuthConverter.class})
class BookingSlotSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreneauService creneauService;

    @MockitoBean
    private PraticienIdentityService praticienIdentityService;

    @MockitoBean
    private PatientIdentityService patientIdentityService;

    @MockitoBean
    private CreneauCalendarQueryService calendarQueryService;

    @MockitoBean
    private RendezVousService rendezVousService;

    @Test
    void patientReadsAvailableSlotsOnly() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        when(creneauService.getAvailableForBooking(eq(2L), eq(3L), any(), any()))
                .thenReturn(List.of(slot(10L, start, StatutCreneau.DISPONIBLE)));

        mockMvc.perform(get("/creneaux/booking/available")
                        .param("praticienId", "2")
                        .param("cliniqueId", "3")
                        .param("debut", start.minusDays(1).toString())
                        .param("fin", start.plusDays(2).toString())
                        .with(authentication(patient())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[1]").doesNotExist())
                .andExpect(jsonPath("$[0].statut").value("DISPONIBLE"));
    }

    @Test
    void patientCannotCreateSlot() throws Exception {
        mockMvc.perform(post("/creneaux/create")
                        .with(authentication(patient()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"praticienId":2,"cliniqueId":3,
                                 "dateDebut":"2035-01-01T08:00:00",
                                 "dateFin":"2035-01-01T08:30:00"}
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void patientCannotBypassWorkflowWithPatientIdInDirectAppointmentRoute() throws Exception {
        mockMvc.perform(post("/rendez-vous/create")
                        .with(authentication(patient()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"creneauId":10,"patientId":999,"type":"CONSULTATION"}
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void patientReadsOnlyAppointmentsResolvedFromJwtSubject() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        when(patientIdentityService.requireActivePatientId("kc-patient"))
                .thenReturn(42L);
        when(rendezVousService.getForPatient(eq(42L), any()))
                .thenReturn(new PageImpl<>(List.of(new PatientRendezVousDto(
                        UUID.randomUUID(),
                        10L,
                        2L,
                        "Trabelsi",
                        "Mohamed",
                        3L,
                        start,
                        start.plusMinutes(30),
                        StatusRendezVous.CONFIRME,
                        TypeRendezVous.CONSULTATION
                ))));

        mockMvc.perform(get("/rendez-vous/me")
                        .with(jwt()
                                .jwt(token -> token.subject("kc-patient"))
                                .authorities(new SimpleGrantedAuthority("ROLE_PATIENT"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].creneauId").value(10))
                .andExpect(jsonPath("$.content[0].patientId").doesNotExist())
                .andExpect(jsonPath("$.content[0].dateDebut").exists());
    }

    private CreneauDto slot(Long id, LocalDateTime start, StatutCreneau status) {
        return CreneauDto.builder().id(id).praticienId(2L).cliniqueId(3L)
                .dateDebut(start).dateFin(start.plusMinutes(30)).statut(status).build();
    }

    private TestingAuthenticationToken patient() {
        return new TestingAuthenticationToken("patient", "n/a", "ROLE_PATIENT");
    }
}
