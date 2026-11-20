package medlink.project.schedulingservice.grpc;

import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import medlink.grpc.creaneau.v1.*;
import medlink.project.schedulingservice.dto.CreneauDto;
import medlink.project.schedulingservice.entities.StatutCreneau;
import medlink.project.schedulingservice.service.CreneauService;
import org.springframework.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class CreneauGrpcEndpoint extends CreneauGrpcServiceGrpc.CreneauGrpcServiceImplBase {

    private final CreneauService creneauService;

    @Override
    public void getCreneau(GetCreneauRequest request, StreamObserver<CreneauResponse> observer) {
        execute(observer, () -> toResponse(creneauService.getById(positiveId(request.getCreneauId()))));
    }

    @Override
    public void checkAvailability(CheckAvailabilityRequest request,
                                  StreamObserver<CheckAvailabilityResponse> observer) {
        execute(observer, () -> {
            long id = positiveId(request.getCreneauId());
            CreneauDto creneau = creneauService.getById(id);
            return CheckAvailabilityResponse.newBuilder()
                    .setCreneauId(id)
                    .setAvailable(creneauService.isDisponible(id))
                    .setStatus(toStatus(creneau.getStatut()))
                    .build();
        });
    }

    @Override
    public void reserveCreneau(ReserveCreneauRequest request,
                               StreamObserver<CreneauResponse> observer) {
        execute(observer, () -> {
            requireUuid(request.getRendezVousId());
            requireText(request.getIdempotencyKey(), "idempotencyKey");
            return toResponse(creneauService.reserver(positiveId(request.getCreneauId())));
        });
    }

    @Override
    public void releaseCreneau(ReleaseCreneauRequest request,
                               StreamObserver<CreneauResponse> observer) {
        execute(observer, () -> {
            requireUuid(request.getRendezVousId());
            requireText(request.getIdempotencyKey(), "idempotencyKey");
            return toResponse(creneauService.liberer(positiveId(request.getCreneauId())));
        });
    }

    private CreneauResponse toResponse(CreneauDto dto) {
        return CreneauResponse.newBuilder()
                .setId(dto.getId())
                .setPraticienId(dto.getPraticienId())
                .setCliniqueId(dto.getCliniqueId())
                .setStartAt(toTimestamp(dto.getDateDebut()))
                .setEndAt(toTimestamp(dto.getDateFin()))
                .setStatus(toStatus(dto.getStatut()))
                .build();
    }

    private Timestamp toTimestamp(LocalDateTime dateTime) {
        var instant = dateTime.toInstant(ZoneOffset.UTC);
        return Timestamp.newBuilder().setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano()).build();
    }

    private CreneauStatus toStatus(StatutCreneau status) {
        return switch (status) {
            case DISPONIBLE -> CreneauStatus.CRENEAU_AVAILABLE;
            case RESERVE -> CreneauStatus.CRENEAU_RESERVED;
            case BLOQUE, INDISPONIBLE -> CreneauStatus.CRENEAU_BLOCKED;
        };
    }

    private long positiveId(long id) {
        if (id <= 0) throw Status.INVALID_ARGUMENT.withDescription("creneauId invalide").asRuntimeException();
        return id;
    }

    private void requireUuid(String id) {
        try { UUID.fromString(id); }
        catch (RuntimeException exception) {
            throw Status.INVALID_ARGUMENT.withDescription("rendezVousId invalide").asRuntimeException();
        }
    }

    private void requireText(String value, String name) {
        if (value == null || value.isBlank())
            throw Status.INVALID_ARGUMENT.withDescription(name + " obligatoire").asRuntimeException();
    }

    private <T> void execute(StreamObserver<T> observer, Operation<T> operation) {
        try {
            observer.onNext(operation.run());
            observer.onCompleted();
        } catch (io.grpc.StatusRuntimeException exception) {
            observer.onError(exception);
        } catch (Exception exception) {
            observer.onError(Status.INTERNAL.withDescription(exception.getMessage())
                    .withCause(exception).asRuntimeException());
        }
    }

    @FunctionalInterface
    private interface Operation<T> { T run(); }
}
