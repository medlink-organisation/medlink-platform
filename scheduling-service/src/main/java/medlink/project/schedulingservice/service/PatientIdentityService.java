package medlink.project.schedulingservice.service;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import medlink.grpc.auth.v1.AuthGrpcServiceGrpc;
import medlink.grpc.auth.v1.GetPatientByIdRequest;
import medlink.project.schedulingservice.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PatientIdentityService {

    private final AuthGrpcServiceGrpc.AuthGrpcServiceBlockingStub stub;

    public long requireActivePatientId(String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new BusinessException("Identité Keycloak du patient manquante.");
        }

        try {
            var patient = stub.withDeadlineAfter(3, TimeUnit.SECONDS)
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
            throw new BusinessException("Impossible de résoudre le patient connecté.");
        }
    }
}
