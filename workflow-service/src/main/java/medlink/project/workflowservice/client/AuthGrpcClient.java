package medlink.project.workflowservice.client;

import lombok.RequiredArgsConstructor;
import medlink.grpc.auth.v1.AuthGrpcServiceGrpc;
import medlink.grpc.auth.v1.GetUserContactRequest;
import medlink.grpc.auth.v1.GetPatientByIdRequest;
import medlink.grpc.auth.v1.GetPatientsByBusinessIdsRequest;
import medlink.grpc.auth.v1.PatientResponse;
import medlink.grpc.auth.v1.UserContactResponse;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthGrpcClient {

    private final AuthGrpcServiceGrpc.AuthGrpcServiceBlockingStub stub;

    public UserContactResponse getUserContact(String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new IllegalArgumentException("keycloakId obligatoire");
        }

        GetUserContactRequest request = GetUserContactRequest.newBuilder()
                .setKeycloakId(keycloakId)
                .build();

        return stub.withDeadlineAfter(3, TimeUnit.SECONDS)
                .getUserContact(request);
    }

    public PatientResponse resolveActivePatient(String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new IllegalArgumentException("keycloakId obligatoire");
        }

        return stub.withDeadlineAfter(3, TimeUnit.SECONDS)
                .resolveActivePatient(GetPatientByIdRequest.newBuilder()
                        .setKeycloakId(keycloakId)
                        .build());
    }

    public PatientResponse getPatientByBusinessId(long patientId) {
        if (patientId <= 0) {
            throw new IllegalArgumentException("patientId doit être positif");
        }
        return stub.withDeadlineAfter(3, TimeUnit.SECONDS)
                .getPatientsByBusinessIds(GetPatientsByBusinessIdsRequest.newBuilder()
                        .addBusinessPatientIds(patientId)
                        .build())
                .getPatientsList()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Patient métier introuvable : " + patientId
                ));
    }
}
