package medlink.project.authservice.service;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import medlink.project.authservice.dto.CreatePraticienRequest;
import medlink.project.authservice.dto.PraticienResponse;
import medlink.project.authservice.grpc.PraticienGrpcClient;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PraticienRegistrationService {

    private final KeycloakUserService keycloakUserService;
    private final PraticienGrpcClient praticienGrpcClient;

    public PraticienResponse createPraticien(
            CreatePraticienRequest request,
            String adminToken
    ) {

        String keycloakId = keycloakUserService.createUser(request);

        log.info(
                "Compte Keycloak créé : keycloakId={}",
                keycloakId
        );

        PraticienResponse praticien;

        try {

            praticien = praticienGrpcClient.create(
                    keycloakId,
                    request,
                    adminToken
            );

        } catch (StatusRuntimeException e) {

            Status.Code code = e.getStatus().getCode();

            log.error(
                    "Erreur gRPC lors de la création du praticien : keycloakId={}, status={}",
                    keycloakId,
                    code,
                    e
            );

            if (code == Status.Code.INVALID_ARGUMENT
                    || code == Status.Code.FAILED_PRECONDITION
                    || code == Status.Code.ALREADY_EXISTS) {

                keycloakUserService.deleteUser(keycloakId);

                log.warn(
                        "Compensation Keycloak effectuée : keycloakId={}",
                        keycloakId
                );
            }

            throw e;
        }

        keycloakUserService.assignPraticienRole(keycloakId);

        keycloakUserService.enableUser(keycloakId);

        log.info(
                "Compte praticien activé : keycloakId={}",
                keycloakId
        );

        sendActivationEmail(keycloakId);

        return praticien;
    }

    private void sendActivationEmail(String keycloakId) {

        try {

            keycloakUserService.sendActivationEmail(keycloakId);

            log.info(
                    "Email d'activation demandé à Keycloak : keycloakId={}",
                    keycloakId
            );

        } catch (Exception e) {

            log.error(
                    "Échec de l'envoi de l'email d'activation : keycloakId={}",
                    keycloakId,
                    e
            );
        }
    }

    public void deleteKeycloakAccount(String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new IllegalArgumentException("keycloakId obligatoire");
        }
        keycloakUserService.deleteUser(keycloakId);
    }
}
