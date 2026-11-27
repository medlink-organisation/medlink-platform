package medlink.project.medicalrecordservice.security;

import medlink.grpc.praticien.v1.PraticienGrpcResponse;
import medlink.grpc.praticien.v1.PraticienGrpcServiceGrpc;
import medlink.grpc.rendezvous.v1.RendezVousGrpcServiceGrpc;
import medlink.grpc.rendezvous.v1.RendezVousResponse;
import medlink.grpc.rendezvous.v1.RendezVousStatus;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import medlink.project.medicalrecordservice.security.authorization.AuthenticatedUserService;
import medlink.project.medicalrecordservice.security.authorization.JwtMedicalRecordAccessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtMedicalRecordAccessServiceTest {

    @Mock
    private AuthenticatedUserService authenticatedUserService;
    @Mock
    private DossierMedicalRepository dossierRepository;
    @Mock
    private PraticienGrpcServiceGrpc.PraticienGrpcServiceBlockingStub praticienStub;
    @Mock
    private RendezVousGrpcServiceGrpc.RendezVousGrpcServiceBlockingStub rendezVousStub;

    private JwtMedicalRecordAccessService service;

    @BeforeEach
    void setUp() {
        service = new JwtMedicalRecordAccessService(
                authenticatedUserService,
                dossierRepository,
                praticienStub,
                rendezVousStub
        );
    }

    @Test
    void patientCanReadOnlyOwnRecord() {
        when(authenticatedUserService.requireAuthenticatedJwt()).thenReturn(authentication(
                "patient-sub",
                "PATIENT",
                42L
        ));
        when(authenticatedUserService.requirePatientId()).thenReturn(42L);
        when(dossierRepository.findById(10L)).thenReturn(Optional.of(
                DossierMedical.builder().id(10L).patientId(42L).build()
        ));

        service.checkCanRead(10L);

        when(authenticatedUserService.requirePatientId()).thenReturn(99L);
        assertThatThrownBy(() -> service.checkCanRead(10L))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void practitionerCanAttachOnlyToCompletedOwnAppointment() {
        UUID rendezVousId = UUID.randomUUID();
        when(authenticatedUserService.requireAuthenticatedJwt()).thenReturn(authentication(
                "praticien-sub",
                "PRATICIEN",
                null
        ));
        when(praticienStub.withDeadlineAfter(3, TimeUnit.SECONDS)).thenReturn(praticienStub);
        when(rendezVousStub.withDeadlineAfter(3, TimeUnit.SECONDS)).thenReturn(rendezVousStub);
        when(praticienStub.getPraticienByKeycloakId(any())).thenReturn(
                PraticienGrpcResponse.newBuilder().setId(7L).setActif(true).build()
        );
        when(rendezVousStub.getRendezVous(any())).thenReturn(
                RendezVousResponse.newBuilder()
                        .setId(rendezVousId.toString())
                        .setPatientId(42L)
                        .setPraticienId(7L)
                        .setStatus(RendezVousStatus.RDV_COMPLETED)
                        .build()
        );
        when(dossierRepository.findByPatientId(42L)).thenReturn(Optional.of(
                DossierMedical.builder().id(10L).patientId(42L).build()
        ));

        assertThat(service.requireRecordIdForAppointment(rendezVousId)).isEqualTo(10L);
    }

    @Test
    void practitionerCannotAttachToAnotherPractitionersAppointment() {
        UUID rendezVousId = UUID.randomUUID();
        when(authenticatedUserService.requireAuthenticatedJwt()).thenReturn(authentication(
                "praticien-sub",
                "PRATICIEN",
                null
        ));
        when(praticienStub.withDeadlineAfter(3, TimeUnit.SECONDS)).thenReturn(praticienStub);
        when(rendezVousStub.withDeadlineAfter(3, TimeUnit.SECONDS)).thenReturn(rendezVousStub);
        when(praticienStub.getPraticienByKeycloakId(any())).thenReturn(
                PraticienGrpcResponse.newBuilder().setId(7L).setActif(true).build()
        );
        when(rendezVousStub.getRendezVous(any())).thenReturn(
                RendezVousResponse.newBuilder()
                        .setId(rendezVousId.toString())
                        .setPatientId(42L)
                        .setPraticienId(8L)
                        .setStatus(RendezVousStatus.RDV_COMPLETED)
                        .build()
        );

        assertThatThrownBy(() -> service.requireRecordIdForAppointment(rendezVousId))
                .isInstanceOf(AccessDeniedException.class);
    }

    private JwtAuthenticationToken authentication(
            String subject,
            String role,
            Long patientId
    ) {
        Jwt.Builder builder = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject(subject);
        if (patientId != null) {
            builder.claim("patient_id", patientId);
        }
        return new JwtAuthenticationToken(
                builder.build(),
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}
