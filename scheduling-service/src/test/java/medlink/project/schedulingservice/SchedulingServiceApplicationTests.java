package medlink.project.schedulingservice;

import medlink.project.schedulingservice.dto.CreneauDto;
import medlink.project.schedulingservice.dto.RendezVousDto;
import medlink.project.schedulingservice.entities.StatusRendezVous;
import medlink.project.schedulingservice.entities.StatutCreneau;
import medlink.project.schedulingservice.entities.TypeRendezVous;
import medlink.project.schedulingservice.service.CreneauService;
import medlink.project.schedulingservice.service.RendezVousService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.config.import=optional:configserver:http://localhost:8888",
        "spring.cloud.config.enabled=false",
        "spring.cloud.vault.enabled=false",
        "spring.cloud.bus.enabled=false",
        "spring.cloud.service-registry.auto-registration.enabled=false",
        "eureka.client.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:scheduling;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:9999/jwks"
})
class SchedulingServiceApplicationTests {

    @Autowired
    private CreneauService creneauService;

    @Autowired
    private RendezVousService rendezVousService;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    void reservationEtAnnulationSynchronisentLeCreneau() {
        CreneauDto creneau = creneauService.create(CreneauDto.builder()
                .praticienId(10L)
                .cliniqueId(20L)
                .dateDebut(LocalDateTime.now().plusDays(2))
                .dateFin(LocalDateTime.now().plusDays(2).plusMinutes(30))
                .build());

        RendezVousDto rendezVous = rendezVousService.reserver(RendezVousDto.builder()
                .creneauId(creneau.getId())
                .patientId(30L)
                .type(TypeRendezVous.CONSULTATION)
                .build());

        assertThat(rendezVous.getStatus()).isEqualTo(StatusRendezVous.CONFIRME);
        assertThat(creneauService.getById(creneau.getId()).getStatut())
                .isEqualTo(StatutCreneau.RESERVE);

        rendezVousService.annuler(rendezVous.getId());

        assertThat(rendezVousService.getById(rendezVous.getId()).getStatus())
                .isEqualTo(StatusRendezVous.ANNULE);
        assertThat(creneauService.getById(creneau.getId()).getStatut())
                .isEqualTo(StatutCreneau.DISPONIBLE);
    }

}
