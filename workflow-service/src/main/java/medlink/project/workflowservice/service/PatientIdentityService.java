package medlink.project.workflowservice.service;

import lombok.RequiredArgsConstructor;
import medlink.grpc.auth.v1.PatientResponse;
import medlink.project.workflowservice.client.AuthGrpcClient;
import medlink.project.workflowservice.exception.WorkflowStateException;
import medlink.project.workflowservice.exception.WorkflowValidationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientIdentityService {

    private final AuthGrpcClient authGrpcClient;

    public long requireActivePatientId(Jwt jwt) {
        if (jwt == null || jwt.getSubject() == null || jwt.getSubject().isBlank()) {
            throw new WorkflowValidationException("L'identité du patient est absente du jeton.");
        }

        PatientResponse patient = authGrpcClient.resolveActivePatient(jwt.getSubject());
        if (!patient.getId().equals(jwt.getSubject())) {
            throw new WorkflowStateException("L'identité du patient n'a pas pu être vérifiée.");
        }
        if (!patient.getEnabled()) {
            throw new WorkflowStateException("Le compte patient est désactivé.");
        }

        long patientId = patient.getBusinessPatientId();
        if (patientId <= 0) {
            throw new WorkflowStateException(
                    "Aucun identifiant patient métier valide n'est associé à ce compte.");
        }
        return patientId;
    }
}
