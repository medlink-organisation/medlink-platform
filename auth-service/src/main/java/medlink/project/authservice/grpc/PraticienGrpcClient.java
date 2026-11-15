package medlink.project.authservice.grpc;

import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;

import lombok.RequiredArgsConstructor;

import medlink.grpc.praticien.v1.CreatePraticienGrpcRequest;
import medlink.grpc.praticien.v1.PraticienGrpcResponse;
import medlink.grpc.praticien.v1.PraticienGrpcServiceGrpc;

import medlink.project.authservice.dto.CreatePraticienRequest;
import medlink.project.authservice.dto.PraticienResponse;

import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PraticienGrpcClient {

    private final PraticienGrpcServiceGrpc.PraticienGrpcServiceBlockingStub stub;

    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    private static final Metadata.Key<String> AUTHORIZATION =
            Metadata.Key.of(
                    "authorization",
                    Metadata.ASCII_STRING_MARSHALLER
            );

    public PraticienResponse create(
            String keycloakId,
            CreatePraticienRequest request,
            String accessToken
    ) {

        CreatePraticienGrpcRequest grpcRequest =
                CreatePraticienGrpcRequest.newBuilder()
                        .setKeycloakId(keycloakId)
                        .setNom(request.nom())
                        .setPrenom(request.prenom())
                        .setEmail(request.email())
                        .setPhone(request.phone())
                        .setNumeroOrdre(request.numeroOrdre())
                        .setSpecialiteId(request.specialiteId())
                        .build();

        Metadata headers = new Metadata();

        headers.put(
                AUTHORIZATION,
                "Bearer " + accessToken
        );

        var authenticatedStub = stub.withInterceptors(
                MetadataUtils.newAttachHeadersInterceptor(headers)
        );

        PraticienGrpcResponse response = circuitBreakerFactory
                .create("praticienGrpc")
                .run(
                        () -> authenticatedStub
                                .withDeadlineAfter(5, TimeUnit.SECONDS)
                                .createPraticien(grpcRequest),

                        throwable -> {
                            if (throwable instanceof StatusRuntimeException grpcException) {
                                throw grpcException;
                            }

                            throw new IllegalStateException(
                                    "Échec de communication avec praticien-service",
                                    throwable
                            );
                        }
                );

        return new PraticienResponse(
                response.getId(),
                response.getKeycloakId(),
                response.getNom(),
                response.getPrenom(),
                response.getEmail(),
                response.getPhone(),
                response.getNumeroOrdre(),
                response.getSpecialiteId(),
                response.getActif()
        );
    }
}