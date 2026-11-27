package medlink.project.schedulingservice.service;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medlink.grpc.auth.v1.AuthGrpcServiceGrpc;
import medlink.grpc.auth.v1.GetPatientByIdRequest;
import medlink.grpc.auth.v1.GetPatientsByBusinessIdsRequest;
import medlink.project.schedulingservice.exception.BusinessException;
import medlink.project.schedulingservice.exception.ExternalServiceException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientIdentityService {

    private final AuthGrpcServiceGrpc.AuthGrpcServiceBlockingStub stub;

    public record PatientIdentity(long id, String nom, String prenom) {
    }

    public Map<Long, PatientIdentity> getByBusinessIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        try {
            var response = stub.withDeadlineAfter(10, TimeUnit.SECONDS)
                    .getPatientsByBusinessIds(
                            GetPatientsByBusinessIdsRequest.newBuilder()
                                    .addAllBusinessPatientIds(ids)
                                    .build()
                    );

            Map<Long, PatientIdentity> patients = new LinkedHashMap<>();
            response.getPatientsList().forEach(patient -> patients.put(
                    patient.getBusinessPatientId(),
                    new PatientIdentity(
                            patient.getBusinessPatientId(),
                            patient.getLastName(),
                            patient.getFirstName()
                    )
            ));
            return patients;
        } catch (StatusRuntimeException exception) {
            log.error(
                    "Échec de la résolution groupée des patients via auth-service: {} - {}",
                    exception.getStatus().getCode(),
                    exception.getStatus().getDescription(),
                    exception
            );
            throw new ExternalServiceException(
                    "Le service d'identité patient est temporairement indisponible.",
                    exception
            );
        }
    }

    public long requireActivePatientId(String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new BusinessException("Identité Keycloak du patient manquante.");
        }

        try {
            var patient = stub.withDeadlineAfter(10, TimeUnit.SECONDS)
                    .resolveActivePatient(GetPatientByIdRequest.newBuilder()
                            .setKeycloakId(keycloakId)
                            .build());
            if (!keycloakId.equals(patient.getId())) {
                throw new BusinessException("L'identité du patient n'a pas pu être vérifiée.");
            }
            if (!patient.getEnabled()) {
                throw new BusinessException("Le compte patient est désactivé.");
            }
            if (patient.getBusinessPatientId() <= 0) {
                throw new BusinessException("L'identifiant métier du patient est invalide.");
            }
            return patient.getBusinessPatientId();
        } catch (StatusRuntimeException exception) {
            var status = exception.getStatus();
            log.warn(
                    "Échec de résolution du patient connecté via auth-service: {} - {}",
                    status.getCode(),
                    status.getDescription()
            );
            throw switch (status.getCode()) {
                case UNAUTHENTICATED -> new BusinessException(
                        "L'authentification interservice du patient a échoué.");
                case NOT_FOUND -> new BusinessException(
                        "Aucun compte patient actif ne correspond à cette session.");
                case DEADLINE_EXCEEDED, UNAVAILABLE -> new BusinessException(
                        "Le service d'identité patient est temporairement indisponible.");
                default -> new BusinessException(
                        "Impossible de résoudre le patient connecté.");
            };
        }
    }
}
