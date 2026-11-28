package medlink.project.directoyservice.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import medlink.grpc.directory.v1.ClinicResponse;
import medlink.grpc.directory.v1.DirectoryGrpcServiceGrpc;
import medlink.grpc.directory.v1.GetClinicByIdRequest;
import medlink.project.directoyservice.dto.CliniqueDto;
import medlink.project.directoyservice.service.CliniqueService;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class DirectoryGrpcEndpoint
        extends DirectoryGrpcServiceGrpc.DirectoryGrpcServiceImplBase {

    private final CliniqueService cliniqueService;

    @Override
    public void getClinicById(
            GetClinicByIdRequest request,
            StreamObserver<ClinicResponse> responseObserver
    ) {
        if (request.getCliniqueId() <= 0) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Identifiant de clinique invalide")
                    .asRuntimeException());
            return;
        }
        try {
            CliniqueDto clinique = cliniqueService.getById(request.getCliniqueId());
            responseObserver.onNext(ClinicResponse.newBuilder()
                    .setId(clinique.getId())
                    .setName(clinique.getNom() == null ? "" : clinique.getNom())
                    .build());
            responseObserver.onCompleted();
        } catch (RuntimeException exception) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Clinique introuvable")
                    .asRuntimeException());
        }
    }
}
