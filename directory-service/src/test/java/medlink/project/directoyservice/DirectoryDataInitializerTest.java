package medlink.project.directoyservice;

import medlink.project.directoyservice.entity.AffectationPraticien;
import medlink.project.directoyservice.entity.Bloc;
import medlink.project.directoyservice.entity.Clinique;
import medlink.project.directoyservice.entity.ServiceMedicale;
import medlink.project.directoyservice.repositories.AffectationPraticienRepository;
import medlink.project.directoyservice.repositories.BlocRepository;
import medlink.project.directoyservice.repositories.CliniqueRepository;
import medlink.project.directoyservice.repositories.ServiceMedicaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectoryDataInitializerTest {

    @Mock
    private CliniqueRepository cliniqueRepository;
    @Mock
    private BlocRepository blocRepository;
    @Mock
    private ServiceMedicaleRepository serviceRepository;
    @Mock
    private AffectationPraticienRepository affectationRepository;

    private DirectoyServiceApplication application;

    @BeforeEach
    void setUp() {
        application = new DirectoyServiceApplication();
    }

    @Test
    void initializeDataCreatesTwoObjectsForEveryTable() {
        AtomicLong cliniqueId = new AtomicLong(10L);
        AtomicLong blocId = new AtomicLong(20L);

        when(cliniqueRepository.findByEmailIgnoreCase(any()))
                .thenReturn(Optional.empty());
        when(cliniqueRepository.save(any(Clinique.class)))
                .thenAnswer(invocation -> {
                    Clinique clinique = invocation.getArgument(0);
                    clinique.setId(cliniqueId.getAndIncrement());
                    return clinique;
                });
        when(blocRepository.findByCodeIgnoreCaseAndCliniqueId(any(), any()))
                .thenReturn(Optional.empty());
        when(blocRepository.save(any(Bloc.class)))
                .thenAnswer(invocation -> {
                    Bloc bloc = invocation.getArgument(0);
                    bloc.setIdBloc(blocId.getAndIncrement());
                    return bloc;
                });
        when(serviceRepository.findByCodeIgnoreCaseAndCliniqueId(any(), any()))
                .thenReturn(Optional.empty());
        when(serviceRepository.save(any(ServiceMedicale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(affectationRepository
                .existsByPraticienIdAndClinique_IdAndActifTrueAndDeletedFalse(
                        any(),
                        any()
                ))
                .thenReturn(false);
        when(affectationRepository.save(any(AffectationPraticien.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        application.initializeData(
                cliniqueRepository,
                blocRepository,
                serviceRepository,
                affectationRepository,
                1L,
                2L
        );

        verify(cliniqueRepository, times(2)).save(any(Clinique.class));
        verify(blocRepository, times(2)).save(any(Bloc.class));
        verify(serviceRepository, times(2)).save(any(ServiceMedicale.class));
        verify(affectationRepository, times(2))
                .save(any(AffectationPraticien.class));

        ArgumentCaptor<AffectationPraticien> captor =
                ArgumentCaptor.forClass(AffectationPraticien.class);
        verify(affectationRepository, times(2)).save(captor.capture());
        assertThat(captor.getAllValues())
                .extracting(AffectationPraticien::getPraticienId)
                .containsExactly(1L, 2L);
        assertThat(captor.getAllValues())
                .allSatisfy(affectation -> {
                    assertThat(affectation.getClinique()).isNotNull();
                    assertThat(affectation.isActif()).isTrue();
                    assertThat(affectation.isDeleted()).isFalse();
                    assertThat(affectation.getDateAffectation()).isNotNull();
                });
    }

    @Test
    void initializeDataDoesNotCreateDuplicates() {
        Clinique clinique1 = clinique(10L);
        Clinique clinique2 = clinique(11L);
        Bloc bloc1 = bloc(20L);
        Bloc bloc2 = bloc(21L);

        when(cliniqueRepository.findByEmailIgnoreCase(any()))
                .thenReturn(Optional.of(clinique1), Optional.of(clinique2));
        when(blocRepository.findByCodeIgnoreCaseAndCliniqueId(any(), any()))
                .thenReturn(Optional.of(bloc1), Optional.of(bloc2));
        when(serviceRepository.findByCodeIgnoreCaseAndCliniqueId(any(), any()))
                .thenReturn(
                        Optional.of(new ServiceMedicale()),
                        Optional.of(new ServiceMedicale())
                );
        when(affectationRepository
                .existsByPraticienIdAndClinique_IdAndActifTrueAndDeletedFalse(
                        any(),
                        any()
                ))
                .thenReturn(true);

        application.initializeData(
                cliniqueRepository,
                blocRepository,
                serviceRepository,
                affectationRepository,
                1L,
                2L
        );

        verify(cliniqueRepository, never()).save(any());
        verify(blocRepository, never()).save(any());
        verify(serviceRepository, never()).save(any());
        verify(affectationRepository, never()).save(any());
    }

    private Clinique clinique(Long id) {
        Clinique clinique = new Clinique();
        clinique.setId(id);
        return clinique;
    }

    private Bloc bloc(Long id) {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(id);
        return bloc;
    }
}
