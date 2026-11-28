package medlink.project.schedulingservice.service;

import medlink.project.schedulingservice.dto.AdminRendezVousDto;
import medlink.project.schedulingservice.dto.RendezVousAgendaDto;
import medlink.project.schedulingservice.dto.PatientRendezVousDto;
import medlink.project.schedulingservice.entities.Creneau;
import medlink.project.schedulingservice.entities.RendezVous;
import medlink.project.schedulingservice.entities.StatusRendezVous;
import medlink.project.schedulingservice.entities.TypeRendezVous;
import medlink.project.schedulingservice.mapper.RendezVousMapper;
import medlink.project.schedulingservice.repositories.CreneauRepository;
import medlink.project.schedulingservice.repositories.RendezVousRepository;
import medlink.project.schedulingservice.validator.RendezVousValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RendezVousAdminListTest {

    private RendezVousRepository repository;
    private PatientIdentityService patientIdentityService;
    private PraticienIdentityService praticienIdentityService;
    private RendezVousServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(RendezVousRepository.class);
        patientIdentityService = mock(PatientIdentityService.class);
        praticienIdentityService = mock(PraticienIdentityService.class);
        service = new RendezVousServiceImpl(
                repository,
                mock(CreneauRepository.class),
                mock(RendezVousMapper.class),
                mock(RendezVousValidator.class),
                patientIdentityService,
                praticienIdentityService
        );
    }

    @Test
    void enrichitUnePageAvecUnSeulAppelGroupeParService() {
        long patientId = 4_303_278_309_522_443_300L;
        LocalDateTime debut = LocalDateTime.of(2026, 11, 27, 9, 0);
        Creneau creneau = Creneau.builder()
                .id(15L)
                .praticienId(56L)
                .cliniqueId(3L)
                .dateDebut(debut)
                .dateFin(debut.plusMinutes(30))
                .build();
        List<RendezVous> rendezVous = List.of(
                rendezVous(patientId, creneau, TypeRendezVous.CONSULTATION),
                rendezVous(patientId, creneau, TypeRendezVous.CONTROLE)
        );
        PageRequest pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable))
                .thenReturn(new PageImpl<>(rendezVous, pageable, rendezVous.size()));
        when(patientIdentityService.getByBusinessIds(anyCollection()))
                .thenReturn(Map.of(patientId,
                        new PatientIdentityService.PatientIdentity(
                                patientId, "Ben Ali", "Ahmed")));
        when(praticienIdentityService.getByIds(anyCollection()))
                .thenReturn(Map.of(56L,
                        new PraticienIdentityService.PraticienIdentity(
                                56L, "Trabelsi", "Mohamed")));

        List<AdminRendezVousDto> result = service.getAll(pageable).getContent();

        assertThat(result).hasSize(2).allSatisfy(dto -> {
            assertThat(dto.patientId()).isEqualTo("4303278309522443300");
            assertThat(dto.patientNom()).isEqualTo("Ben Ali");
            assertThat(dto.patientPrenom()).isEqualTo("Ahmed");
            assertThat(dto.praticienNom()).isEqualTo("Trabelsi");
            assertThat(dto.praticienPrenom()).isEqualTo("Mohamed");
            assertThat(dto.creneauId()).isEqualTo(15L);
            assertThat(dto.cliniqueId()).isEqualTo(3L);
            assertThat(dto.dateDebut()).isEqualTo(debut);
            assertThat(dto.dateFin()).isEqualTo(debut.plusMinutes(30));
        });
        verify(patientIdentityService).getByBusinessIds(Set.of(patientId));
        verify(praticienIdentityService).getByIds(Set.of(56L));
    }

    @Test
    void enrichitAgendaPraticienAvecIdentitePatientEtHoraireDuCreneau() {
        long patientId = 4_303_278_309_522_443_300L;
        LocalDateTime debut = LocalDateTime.of(2026, 11, 28, 10, 30);
        LocalDateTime fin = debut.plusMinutes(30);
        Creneau creneau = Creneau.builder()
                .id(142L)
                .praticienId(56L)
                .cliniqueId(3L)
                .dateDebut(debut)
                .dateFin(fin)
                .build();
        RendezVous appointment = rendezVous(patientId, creneau, TypeRendezVous.CONSULTATION);

        when(repository.findAgendaByPraticien(56L, debut.minusDays(1), fin.plusDays(1)))
                .thenReturn(List.of(appointment));
        when(patientIdentityService.getByBusinessIds(anyCollection()))
                .thenReturn(Map.of(patientId,
                        new PatientIdentityService.PatientIdentity(
                                patientId, "Ben Ali", "Ahmed")));

        List<RendezVousAgendaDto> result = service.getAgendaByPraticien(
                56L, debut.minusDays(1), fin.plusDays(1));

        assertThat(result).singleElement().satisfies(dto -> {
            assertThat(dto.patientNom()).isEqualTo("Ben Ali");
            assertThat(dto.patientPrenom()).isEqualTo("Ahmed");
            assertThat(dto.dateDebut()).isEqualTo(debut);
            assertThat(dto.dateFin()).isEqualTo(fin);
        });
        verify(patientIdentityService).getByBusinessIds(List.of(patientId));
    }

    @Test
    void enrichitLesRendezVousPatientAvecLeNomDuPraticien() {
        long patientId = 42L;
        LocalDateTime debut = LocalDateTime.of(2026, 11, 28, 14, 0);
        Creneau creneau = Creneau.builder()
                .id(150L)
                .praticienId(56L)
                .cliniqueId(3L)
                .dateDebut(debut)
                .dateFin(debut.plusMinutes(30))
                .build();
        PageRequest pageable = PageRequest.of(0, 10);
        when(repository.findAllByPatientId(patientId, pageable))
                .thenReturn(new PageImpl<>(
                        List.of(rendezVous(patientId, creneau, TypeRendezVous.CONSULTATION)),
                        pageable,
                        1
                ));
        when(praticienIdentityService.getByIds(anyCollection()))
                .thenReturn(Map.of(56L,
                        new PraticienIdentityService.PraticienIdentity(
                                56L, "Trabelsi", "Mohamed")));

        List<PatientRendezVousDto> result = service.getForPatient(patientId, pageable).getContent();

        assertThat(result).singleElement().satisfies(dto -> {
            assertThat(dto.praticienNom()).isEqualTo("Trabelsi");
            assertThat(dto.praticienPrenom()).isEqualTo("Mohamed");
            assertThat(dto.dateDebut()).isEqualTo(debut);
        });
        verify(praticienIdentityService).getByIds(List.of(56L));
    }

    private RendezVous rendezVous(
            long patientId,
            Creneau creneau,
            TypeRendezVous type
    ) {
        return RendezVous.builder()
                .id(UUID.randomUUID())
                .patientId(patientId)
                .creneau(creneau)
                .type(type)
                .status(StatusRendezVous.TERMINE)
                .build();
    }
}
