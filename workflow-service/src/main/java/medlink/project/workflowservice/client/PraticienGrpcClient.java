package medlink.project.workflowservice.client;

import lombok.RequiredArgsConstructor;
import medlink.grpc.praticien.v1.GetPraticienByIdRequest;
import medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest;
import medlink.grpc.praticien.v1.PraticienGrpcResponse;
import medlink.grpc.praticien.v1.PraticienGrpcServiceGrpc;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PraticienGrpcClient {

    private final PraticienGrpcServiceGrpc.PraticienGrpcServiceBlockingStub stub;

    public PraticienGrpcResponse getById(long praticienId) {
        if (praticienId <= 0) {
            throw new IllegalArgumentException("praticienId doit être positif");
        }

        GetPraticienByIdRequest request = GetPraticienByIdRequest.newBuilder()
                .setPraticienId(praticienId)
                .build();

        return stub.withDeadlineAfter(3, TimeUnit.SECONDS)
                .getPraticienById(request);
    }

    public PraticienGrpcResponse getByKeycloakId(String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new IllegalArgumentException("keycloakId obligatoire");
        }

        return stub.withDeadlineAfter(3, TimeUnit.SECONDS)
                .getPraticienByKeycloakId(GetPraticienByKeycloakIdRequest.newBuilder()
                        .setKeycloakId(keycloakId)
                        .build());
    }
}
