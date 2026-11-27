package medlink.project.schedulingservice;

import medlink.project.auditcore.dto.AuditOperation;
import medlink.project.schedulingservice.dto.CreneauDto;
import medlink.project.schedulingservice.dto.GenerationCreneauxRequest;
import medlink.project.schedulingservice.dto.RendezVousDto;
import medlink.project.schedulingservice.entities.StatusRendezVous;
import medlink.project.schedulingservice.entities.StatutCreneau;
import medlink.project.schedulingservice.entities.TypeRendezVous;
import medlink.project.schedulingservice.exception.BusinessException;
import medlink.project.schedulingservice.service.AuditService;
import medlink.project.schedulingservice.service.CreneauService;
import medlink.project.schedulingservice.service.DirectoryAssignmentClient;
import medlink.project.schedulingservice.service.CreneauCalendarQueryService;
import medlink.project.schedulingservice.service.RendezVousService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {
        "spring.config.import=optional:configserver:http://localhost:8888",
        "spring.cloud.config.enabled=false",
        "spring.cloud.vault.enabled=false",
        "spring.cloud.bus.enabled=false",
        "spring.cloud.service-registry.auto-registration.enabled=false",
        "eureka.client.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:scheduling;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.grpc.server.port=0",
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:9999/jwks"
})
class SchedulingServiceApplicationTests {

    @Autowired
    private CreneauService creneauService;

    @Autowired
    private RendezVousService rendezVousService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private CreneauCalendarQueryService creneauCalendarQueryService;

    @MockitoBean
    private DirectoryAssignmentClient directoryAssignmentClient;

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

    @Test
    void deuxReservationsConcurrentesSurLeMemeCreneauNEnCreentQuUne() throws Exception {
        CreneauDto creneau = creneauService.create(CreneauDto.builder()
                .praticienId(510L)
                .cliniqueId(520L)
                .dateDebut(LocalDateTime.now().plusDays(5))
                .dateFin(LocalDateTime.now().plusDays(5).plusMinutes(30))
                .build());
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger successes = new AtomicInteger();
        AtomicInteger failures = new AtomicInteger();

        CompletableFuture<Void> first = reserveConcurrently(
                creneau.getId(), 530L, ready, start, successes, failures);
        CompletableFuture<Void> second = reserveConcurrently(
                creneau.getId(), 531L, ready, start, successes, failures);

        assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
        start.countDown();
        CompletableFuture.allOf(first, second).get(10, TimeUnit.SECONDS);

        assertThat(successes).hasValue(1);
        assertThat(failures).hasValue(1);
        assertThat(rendezVousService.getByCreneau(
                creneau.getId(), PageRequest.of(0, 10))).hasSize(1);
        assertThat(creneauService.getById(creneau.getId()).getStatut())
                .isEqualTo(StatutCreneau.RESERVE);
    }

    @Test
    void enversEnregistreCreneauEtRendezVous() {
        CreneauDto creneau = creneauService.create(CreneauDto.builder()
                .praticienId(110L)
                .cliniqueId(120L)
                .dateDebut(LocalDateTime.now().plusDays(4))
                .dateFin(LocalDateTime.now().plusDays(4).plusMinutes(30))
                .build());

        RendezVousDto rendezVous = rendezVousService.reserver(RendezVousDto.builder()
                .creneauId(creneau.getId())
                .patientId(130L)
                .type(TypeRendezVous.CONTROLE)
                .build());

        assertThat(auditService.getHistory("creneau", creneau.getId().toString(), PageRequest.of(0, 20)))
                .isNotEmpty()
                .allSatisfy(revision -> assertThat(revision.serviceName()).isEqualTo("scheduling-service"));

        assertThat(auditService.getHistory(
                "rendez-vous", rendezVous.getId().toString(), PageRequest.of(0, 20)
        ).getContent())
                .singleElement()
                .satisfies(revision -> {
                    assertThat(revision.operation()).isEqualTo(AuditOperation.ADD);
                    assertThat(revision.entityName()).isEqualTo("RendezVous");
                    assertThat(revision.changes()).isNotEmpty();
                });
    }

    @Test
    @Transactional
    void generationGroupeeCreeUniquementLesDatesSelectionnees() {
        LocalDate firstDate = LocalDate.now().plusWeeks(2);
        GenerationCreneauxRequest request = new GenerationCreneauxRequest(
                210L,
                220L,
                List.of(firstDate, firstDate.plusDays(2)),
                LocalTime.of(8, 0),
                LocalTime.of(14, 0),
                30
        );

        var creation = creneauService.generate(request);

        assertThat(creation.createdCount()).isEqualTo(24);
        assertThat(creneauService.getCalendar(
                210L,
                220L,
                firstDate.atStartOfDay(),
                firstDate.plusDays(3).atStartOfDay()
        )).hasSize(24);
    }

    @Test
    @Transactional
    void generationGroupeeAlimenteLeCalendrierMensuel() {
        LocalDate date = LocalDate.now().plusWeeks(1);
        creneauService.generate(new GenerationCreneauxRequest(
                310L, 320L, List.of(date), LocalTime.of(8, 0), LocalTime.of(12, 0), 30
        ));

        assertThat(creneauService.getCalendar(310L, null, date.atStartOfDay(), date.plusDays(1).atStartOfDay()))
                .hasSize(8);
        assertThat(creneauCalendarQueryService.getMonth(310L, java.time.YearMonth.from(date)).jours())
                .filteredOn(day -> day.date().equals(date))
                .singleElement()
                .satisfies(day -> {
                    assertThat(day.statutPlanning()).isEqualTo("TRAVAIL");
                    assertThat(day.creneauxDisponibles()).isEqualTo(8);
                    assertThat(day.plages()).hasSize(1);
                });
    }

    @Test
    @Transactional
    void generationGroupeeRefuseToutLeLotSiUneDateEstEnConflit() {
        LocalDate firstDate = LocalDate.now().plusWeeks(3);
        LocalDate conflictingDate = firstDate.plusDays(1);
        creneauService.generate(new GenerationCreneauxRequest(
                410L, 420L, List.of(conflictingDate), LocalTime.of(8, 0), LocalTime.of(9, 0), 30
        ));

        assertThatThrownBy(() -> creneauService.generate(new GenerationCreneauxRequest(
                410L, 420L, List.of(firstDate, conflictingDate), LocalTime.of(8, 0), LocalTime.of(9, 0), 30
        ))).isInstanceOf(BusinessException.class)
                .hasMessageContaining(conflictingDate.toString());

        assertThat(creneauService.getCalendar(
                410L, 420L, firstDate.atStartOfDay(), conflictingDate.plusDays(1).atStartOfDay()
        )).hasSize(2).allMatch(slot -> slot.getDateDebut().toLocalDate().equals(conflictingDate));
    }

    private CompletableFuture<Void> reserveConcurrently(
            long creneauId,
            long patientId,
            CountDownLatch ready,
            CountDownLatch start,
            AtomicInteger successes,
            AtomicInteger failures) {
        return CompletableFuture.runAsync(() -> {
            ready.countDown();
            try {
                start.await(5, TimeUnit.SECONDS);
                rendezVousService.reserver(RendezVousDto.builder()
                        .creneauId(creneauId)
                        .patientId(patientId)
                        .type(TypeRendezVous.CONSULTATION)
                        .build());
                successes.incrementAndGet();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                failures.incrementAndGet();
            } catch (RuntimeException exception) {
                failures.incrementAndGet();
            }
        });
    }

}
