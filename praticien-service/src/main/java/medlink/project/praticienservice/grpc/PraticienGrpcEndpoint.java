package medlink.project.praticienservice.grpc;


import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import medlink.grpc.praticien.v1.CreatePraticienGrpcRequest;
import medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest;
import medlink.grpc.praticien.v1.PraticienGrpcResponse;
import medlink.grpc.praticien.v1.PraticienGrpcServiceGrpc;
import medlink.project.praticienservice.dto.PraticienDto;
import medlink.project.praticienservice.service.PraticienService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.grpc.server.service.GrpcService;

import java.util.NoSuchElementException;

@GrpcService
@RequiredArgsConstructor
public class PraticienGrpcEndpoint
        extends PraticienGrpcServiceGrpc.PraticienGrpcServiceImplBase {

    private final PraticienService praticienService;

    private final PraticienGrpcMapper grpcMapper;

    @Override
    public void createPraticien(
            CreatePraticienGrpcRequest request,
            StreamObserver<PraticienGrpcResponse> responseObserver
    ) {

        try {

            // STEP 1 : Validation
            if (request.getKeycloakId().isBlank()
                    || request.getNom().isBlank()
                    || request.getPrenom().isBlank()
                    || request.getEmail().isBlank()
                    || request.getSpecialiteId() <= 0) {

                throw Status.INVALID_ARGUMENT
                        .withDescription("Données praticien invalides")
                        .asRuntimeException();
            }

            PraticienDto dto = grpcMapper.toDto(request);

            PraticienDto created = praticienService.create(dto);

            PraticienGrpcResponse response =
                    grpcMapper.toGrpc(created);

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (StatusRuntimeException e) {

            responseObserver.onError(e);

        } catch (DataIntegrityViolationException e) {

            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription(
                                    "Conflit de données praticien"
                            )
                            .asRuntimeException()
            );

        } catch (IllegalArgumentException e) {

            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Données invalides")
                            .asRuntimeException()
            );

        } catch (Exception e) {

            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription(
                                    "Erreur interne lors de la création"
                            )
                            .asRuntimeException()
            );
        }
    }
    @Override
    public void getPraticienByKeycloakId(
            GetPraticienByKeycloakIdRequest request,
            StreamObserver<PraticienGrpcResponse> responseObserver
    ) {
        try {
            PraticienDto dto = praticienService.getByKeycloakId(
                    request.getKeycloakId()
            );

            responseObserver.onNext(grpcMapper.toGrpc(dto));
            responseObserver.onCompleted();

        } catch (NoSuchElementException e) {

            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Praticien introuvable")
                            .asRuntimeException()
            );

        } catch (Exception e) {

            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Erreur lors de la recherche du praticien")
                            .asRuntimeException()
            );
        }
    }}