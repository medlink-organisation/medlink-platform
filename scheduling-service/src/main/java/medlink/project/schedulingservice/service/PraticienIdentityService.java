package medlink.project.schedulingservice.service;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest;
import medlink.grpc.praticien.v1.PraticienGrpcServiceGrpc;
import medlink.project.schedulingservice.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PraticienIdentityService {

    private final PraticienGrpcServiceGrpc.PraticienGrpcServiceBlockingStub stub;

    public long requireActivePraticienId(String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new BusinessException("Identité Keycloak du praticien manquante.");
        }

        try {
            var response = stub.withDeadlineAfter(3, TimeUnit.SECONDS)
                    .getPraticienByKeycloakId(GetPraticienByKeycloakIdRequest.newBuilder()
                            .setKeycloakId(keycloakId)
                            .build());
            if (!response.getActif()) {
                throw new BusinessException("Le compte praticien est inactif.");
            }
            return response.getId();
        }catch (StatusRuntimeException exception) {

            log.warn(
                    "Erreur gRPC praticien-service : {} - {}",
                    exception.getStatus().getCode(),
                    exception.getStatus().getDescription(),
                    exception
            );

            throw new BusinessException(
                    "Impossible de résoudre le praticien connecté."
            );
        }
    }
}
