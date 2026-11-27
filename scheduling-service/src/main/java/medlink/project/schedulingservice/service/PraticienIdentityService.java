package medlink.project.schedulingservice.service;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest;
import medlink.grpc.praticien.v1.GetPraticiensByIdsRequest;
import medlink.grpc.praticien.v1.PraticienGrpcServiceGrpc;
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
public class PraticienIdentityService {

    private final PraticienGrpcServiceGrpc.PraticienGrpcServiceBlockingStub stub;

    public record PraticienIdentity(long id, String nom, String prenom) {
    }

    public Map<Long, PraticienIdentity> getByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        try {
            var response = stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                    .getPraticiensByIds(GetPraticiensByIdsRequest.newBuilder()
                            .addAllPraticienIds(ids)
                            .build());

            Map<Long, PraticienIdentity> praticiens = new LinkedHashMap<>();
            response.getPraticiensList().forEach(praticien -> praticiens.put(
                    praticien.getId(),
                    new PraticienIdentity(
                            praticien.getId(),
                            praticien.getNom(),
                            praticien.getPrenom()
                    )
            ));
            return praticiens;
        } catch (StatusRuntimeException exception) {
            log.error(
                    "Échec de la résolution groupée des praticiens: {} - {}",
                    exception.getStatus().getCode(),
                    exception.getStatus().getDescription(),
                    exception
            );
            throw new ExternalServiceException(
                    "Le service des praticiens est temporairement indisponible.",
                    exception
            );
        }
    }

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
