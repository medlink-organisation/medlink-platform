package medlink.project.authservice.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medlink.grpc.auth.v1.AuthGrpcServiceGrpc;
import medlink.grpc.auth.v1.BooleanResponse;
import medlink.grpc.auth.v1.GetPatientByEmailRequest;
import medlink.grpc.auth.v1.GetPatientByIdRequest;
import medlink.grpc.auth.v1.GetPatientsRequest;
import medlink.grpc.auth.v1.GetUserContactRequest;
import medlink.grpc.auth.v1.PatientPageResponse;
import medlink.grpc.auth.v1.PatientResponse;
import medlink.grpc.auth.v1.PatientRolesResponse;
import medlink.grpc.auth.v1.UserContactResponse;
import medlink.project.authservice.service.KeycloakUserService;
import jakarta.ws.rs.NotFoundException;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Supplier;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class AuthGrpcEndpoint extends AuthGrpcServiceGrpc.AuthGrpcServiceImplBase {

    private final KeycloakUserService keycloakUserService;

    @Override
    public void getUserContact(
            GetUserContactRequest request,
            StreamObserver<UserContactResponse> responseObserver
    ) {
        try {
            String keycloakId = requireKeycloakId(request);
            UserRepresentation user = keycloakUserService.getUser(keycloakId);

            UserContactResponse response = UserContactResponse.newBuilder()
                    .setKeycloakId(valueOrEmpty(user.getId()))
                    .setEmail(valueOrEmpty(user.getEmail()))
                    .setDisplayName(displayName(user))
                    .setEnabled(Boolean.TRUE.equals(user.isEnabled()))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exception) {
            responseObserver.onError(exception);
        } catch (NotFoundException exception) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Utilisateur Keycloak introuvable")
                    .asRuntimeException());
        } catch (Exception exception) {
            log.error("Erreur lors de la lecture de l'utilisateur dans Keycloak", exception);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Erreur interne lors de la lecture de l'utilisateur")
                    .asRuntimeException());
        }
    }

    @Override
    public void getPatientById(
            GetPatientByIdRequest request,
            StreamObserver<PatientResponse> responseObserver
    ) {
        execute(responseObserver, () -> toGrpc(
                keycloakUserService.getPatientById(request.getKeycloakId())
        ));
    }

    @Override
    public void getAllPatients(
            GetPatientsRequest request,
            StreamObserver<PatientPageResponse> responseObserver
    ) {
        execute(responseObserver, () -> {
            int max = request.getMax() == 0 ? 20 : request.getMax();
            var page = keycloakUserService.getAllPatients(request.getFirst(), max);

            return PatientPageResponse.newBuilder()
                    .addAllContent(page.content().stream().map(this::toGrpc).toList())
                    .setFirst(page.first())
                    .setMax(page.max())
                    .setHasNext(page.hasNext())
                    .build();
        });
    }

    @Override
    public void getPatientByEmail(
            GetPatientByEmailRequest request,
            StreamObserver<PatientResponse> responseObserver
    ) {
        execute(responseObserver, () -> toGrpc(
                keycloakUserService.getPatientByEmail(request.getEmail())
        ));
    }

    @Override
    public void existsPatientById(
            GetPatientByIdRequest request,
            StreamObserver<BooleanResponse> responseObserver
    ) {
        execute(responseObserver, () -> booleanResponse(
                keycloakUserService.existsPatientById(request.getKeycloakId())
        ));
    }

    @Override
    public void existsPatientByEmail(
            GetPatientByEmailRequest request,
            StreamObserver<BooleanResponse> responseObserver
    ) {
        execute(responseObserver, () -> booleanResponse(
                keycloakUserService.existsPatientByEmail(request.getEmail())
        ));
    }

    @Override
    public void isPatientActive(
            GetPatientByIdRequest request,
            StreamObserver<BooleanResponse> responseObserver
    ) {
        execute(responseObserver, () -> booleanResponse(
                keycloakUserService.isPatientActive(request.getKeycloakId())
        ));
    }

    @Override
    public void enablePatient(
            GetPatientByIdRequest request,
            StreamObserver<PatientResponse> responseObserver
    ) {
        execute(responseObserver, () -> {
            keycloakUserService.enablePatient(request.getKeycloakId());
            return toGrpc(keycloakUserService.getPatientById(request.getKeycloakId()));
        });
    }

    @Override
    public void disablePatient(
            GetPatientByIdRequest request,
            StreamObserver<PatientResponse> responseObserver
    ) {
        execute(responseObserver, () -> {
            keycloakUserService.disablePatient(request.getKeycloakId());
            return toGrpc(keycloakUserService.getPatientById(request.getKeycloakId()));
        });
    }

    @Override
    public void getPatientRoles(
            GetPatientByIdRequest request,
            StreamObserver<PatientRolesResponse> responseObserver
    ) {
        execute(responseObserver, () -> PatientRolesResponse.newBuilder()
                .addAllRoles(keycloakUserService.getPatientRoles(request.getKeycloakId()))
                .build());
    }

    @Override
    public void updatePatientContact(
            medlink.grpc.auth.v1.UpdatePatientContactRequest request,
            StreamObserver<PatientResponse> responseObserver
    ) {
        execute(responseObserver, () -> {
            var contact = new medlink.project.authservice.dto.UpdatePatientContactRequest(
                    request.hasEmail() ? request.getEmail() : null,
                    request.hasFirstName() ? request.getFirstName() : null,
                    request.hasLastName() ? request.getLastName() : null
            );
            return toGrpc(keycloakUserService.updatePatientContact(
                    request.getKeycloakId(),
                    contact
            ));
        });
    }

    private <T> void execute(
            StreamObserver<T> responseObserver,
            Supplier<T> operation
    ) {
        try {
            responseObserver.onNext(operation.get());
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exception) {
            responseObserver.onError(exception);
        } catch (ResponseStatusException exception) {
            responseObserver.onError(toGrpcStatus(exception).asRuntimeException());
        } catch (IllegalArgumentException exception) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(exception.getMessage())
                    .asRuntimeException());
        } catch (NotFoundException exception) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Ressource Keycloak introuvable")
                    .asRuntimeException());
        } catch (Exception exception) {
            log.error("Erreur interne de l'endpoint gRPC Auth", exception);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Erreur interne du service Auth")
                    .asRuntimeException());
        }
    }

    private Status toGrpcStatus(ResponseStatusException exception) {
        Status status;
        if (exception.getStatusCode() == HttpStatus.BAD_REQUEST) {
            status = Status.INVALID_ARGUMENT;
        } else if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
            status = Status.NOT_FOUND;
        } else if (exception.getStatusCode() == HttpStatus.CONFLICT) {
            status = Status.ALREADY_EXISTS;
        } else {
            status = Status.INTERNAL;
        }
        return status.withDescription(exception.getReason());
    }

    private PatientResponse toGrpc(
            medlink.project.authservice.dto.PatientResponse patient
    ) {
        return PatientResponse.newBuilder()
                .setId(valueOrEmpty(patient.id()))
                .setUsername(valueOrEmpty(patient.username()))
                .setEmail(valueOrEmpty(patient.email()))
                .setFirstName(valueOrEmpty(patient.firstName()))
                .setLastName(valueOrEmpty(patient.lastName()))
                .setEnabled(patient.enabled())
                .setEmailVerified(patient.emailVerified())
                .build();
    }

    private BooleanResponse booleanResponse(boolean value) {
        return BooleanResponse.newBuilder().setValue(value).build();
    }

    private String requireKeycloakId(GetUserContactRequest request) {
        String keycloakId = request.getKeycloakId();

        if (keycloakId == null || keycloakId.isBlank()) {
            throw Status.INVALID_ARGUMENT
                    .withDescription("keycloakId obligatoire")
                    .asRuntimeException();
        }

        return keycloakId.trim();
    }

    private String displayName(UserRepresentation user) {
        return (valueOrEmpty(user.getFirstName())
                + " "
                + valueOrEmpty(user.getLastName()))
                .trim();
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
