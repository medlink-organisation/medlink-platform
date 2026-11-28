package medlink.project.medicalrecordservice.security.authorization;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest;
import medlink.grpc.praticien.v1.PraticienGrpcServiceGrpc;
import medlink.grpc.rendezvous.v1.GetRendezVousRequest;
import medlink.grpc.rendezvous.v1.RendezVousGrpcServiceGrpc;
import medlink.grpc.rendezvous.v1.RendezVousStatus;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtMedicalRecordAccessService implements MedicalRecordAccessService {

    private final AuthenticatedUserService authenticatedUserService;
    private final DossierMedicalRepository dossierRepository;
    private final PraticienGrpcServiceGrpc.PraticienGrpcServiceBlockingStub praticienStub;
    private final RendezVousGrpcServiceGrpc.RendezVousGrpcServiceBlockingStub rendezVousStub;

    @Override
    public void checkCanCreateForPatient(Long patientId) {
        JwtAuthenticationToken authentication = authenticatedUserService.requireAuthenticatedJwt();
        requirePatient(authentication);
        if (authenticatedUserService.requirePatientId() != patientId) {
            deny();
        }
    }

    @Override
    public void checkCanRead(Long dossierId) {
        JwtAuthenticationToken authentication = authenticatedUserService.requireAuthenticatedJwt();
        requirePatient(authentication);
        DossierMedical dossier = requireDossier(dossierId);
        if (authenticatedUserService.requirePatientId() != dossier.getPatientId()) {
            deny();
        }
    }

    @Override
    public void checkCanWrite(Long dossierId) {
        deny();
    }

    @Override
    public void checkCanAttachResult(Long dossierId, UUID rendezVousId) {
        DossierMedical dossier = requireDossier(dossierId);
        Long authorizedDossierId = requireRecordIdForAppointment(rendezVousId);
        if (!dossier.getId().equals(authorizedDossierId)) {
            deny();
        }
    }

    @Override
    public Long requireRecordIdForAppointment(UUID rendezVousId) {

        JwtAuthenticationToken authentication =
                authenticatedUserService.requireAuthenticatedJwt();

        requireRole(authentication, "PRATICIEN");

        if (rendezVousId == null) {
            deny();
        }

        try {

            var praticien = praticienStub
                    .withDeadlineAfter(3, TimeUnit.SECONDS)
                    .getPraticienByKeycloakId(
                            GetPraticienByKeycloakIdRequest.newBuilder()
                                    .setKeycloakId(
                                            authentication.getToken().getSubject()
                                    )
                                    .build()
                    );

            if (!praticien.getActif()) {

                log.warn("Accès refusé : praticien inactif");

                deny();
            }

            var rendezVous = rendezVousStub
                    .withDeadlineAfter(3, TimeUnit.SECONDS)
                    .getRendezVous(
                            GetRendezVousRequest.newBuilder()
                                    .setRendezVousId(rendezVousId.toString())
                                    .build()
                    );

            if (rendezVous.getPraticienId() != praticien.getId()) {

                log.warn(
                        "Accès refusé : rendez-vous rattaché à un autre praticien"
                );

                deny();
            }

            if (rendezVous.getStatus() != RendezVousStatus.RDV_COMPLETED) {

                log.warn(
                        "Accès refusé : statut du rendez-vous = {}",
                        rendezVous.getStatus()
                );

                deny();
            }

            return dossierRepository
                    .findByPatientId(rendezVous.getPatientId())
                    .map(DossierMedical::getId)
                    .orElseThrow(() -> {

                        log.warn(
                                "Accès refusé : aucun dossier médical correspondant"
                        );

                        return new AccessDeniedException(
                                "Aucun dossier médical correspondant."
                        );
                    });

        } catch (StatusRuntimeException exception) {

            log.warn(
                    "Erreur gRPC pendant la vérification du dossier médical : {} - {}",
                    exception.getStatus().getCode(),
                    exception.getStatus().getDescription()
            );

            throw new AccessDeniedException(
                    "Impossible de vérifier l'accès au dossier médical.",
                    exception
            );
        }
    }

    @Override
    public void checkCanListAll() {
        deny();
    }

    @Override
    public void checkCanListByPractitioner(Long practitionerId) {
        deny();
    }

    private DossierMedical requireDossier(Long dossierId) {
        if (dossierId == null || dossierId <= 0) {
            deny();
        }
        return dossierRepository.findById(dossierId)
                .orElseThrow(() -> new AccessDeniedException("Accès au dossier médical refusé."));
    }

    private void requirePatient(JwtAuthenticationToken authentication) {
        requireRole(authentication, "PATIENT");
    }

    private void requireRole(JwtAuthenticationToken authentication, String role) {
        boolean allowed = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(role) || authority.equals("ROLE_" + role));
        if (!allowed) {
            deny();
        }
    }

    private void deny() {
        throw new AccessDeniedException("Accès au dossier médical refusé.");
    }
}
