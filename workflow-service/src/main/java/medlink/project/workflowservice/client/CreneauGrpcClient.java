package medlink.project.workflowservice.client;

import lombok.RequiredArgsConstructor;
import medlink.grpc.creaneau.v1.CheckAvailabilityRequest;
import medlink.grpc.creaneau.v1.CheckAvailabilityResponse;
import medlink.grpc.creaneau.v1.CreneauGrpcServiceGrpc;
import medlink.grpc.creaneau.v1.CreneauResponse;
import medlink.grpc.creaneau.v1.GetCreneauRequest;
import medlink.grpc.creaneau.v1.ReleaseCreneauRequest;
import medlink.grpc.creaneau.v1.ReserveCreneauRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CreneauGrpcClient {

    private static final long DEADLINE_SECONDS = 3;

    private final CreneauGrpcServiceGrpc.CreneauGrpcServiceBlockingStub stub;

    public CreneauResponse getById(long creneauId) {
        validateId(creneauId, "creneauId");

        GetCreneauRequest request = GetCreneauRequest.newBuilder()
                .setCreneauId(creneauId)
                .build();

        return client().getCreneau(request);
    }

    public CheckAvailabilityResponse checkAvailability(long creneauId) {
        validateId(creneauId, "creneauId");

        CheckAvailabilityRequest request = CheckAvailabilityRequest.newBuilder()
                .setCreneauId(creneauId)
                .build();

        return client().checkAvailability(request);
    }

    public boolean isAvailable(long creneauId) {
        return checkAvailability(creneauId).getAvailable();
    }

    public CreneauResponse reserve(
            long creneauId,
            String rendezVousId,
            String idempotencyKey) {

        validateId(creneauId, "creneauId");
        validateText(rendezVousId, "rendezVousId");
        validateIdempotencyKey(idempotencyKey);

        ReserveCreneauRequest request = ReserveCreneauRequest.newBuilder()
                .setCreneauId(creneauId)
                .setRendezVousId(rendezVousId)
                .setIdempotencyKey(idempotencyKey)
                .build();

        return client().reserveCreneau(request);
    }

    public CreneauResponse release(
            long creneauId,
            String rendezVousId,
            String idempotencyKey) {

        validateId(creneauId, "creneauId");
        validateText(rendezVousId, "rendezVousId");
        validateIdempotencyKey(idempotencyKey);

        ReleaseCreneauRequest request = ReleaseCreneauRequest.newBuilder()
                .setCreneauId(creneauId)
                .setRendezVousId(rendezVousId)
                .setIdempotencyKey(idempotencyKey)
                .build();

        return client().releaseCreneau(request);
    }

    private CreneauGrpcServiceGrpc.CreneauGrpcServiceBlockingStub client() {
        return stub.withDeadlineAfter(DEADLINE_SECONDS, TimeUnit.SECONDS);
    }

    private void validateId(long id, String fieldName) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    fieldName + " doit être strictement positif");
        }
    }

    private void validateIdempotencyKey(String idempotencyKey) {
        validateText(idempotencyKey, "idempotencyKey");
    }

    private void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " obligatoire");
        }
    }
}
