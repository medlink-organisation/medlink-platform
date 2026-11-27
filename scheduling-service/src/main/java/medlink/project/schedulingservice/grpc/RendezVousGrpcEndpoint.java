package medlink.project.schedulingservice.grpc;

import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import medlink.grpc.rendezvous.v1.*;
import medlink.project.schedulingservice.dto.CreneauDto;
import medlink.project.schedulingservice.dto.RendezVousDto;
import medlink.project.schedulingservice.entities.StatusRendezVous;
import medlink.project.schedulingservice.entities.TypeRendezVous;
import medlink.project.schedulingservice.exception.BusinessException;
import medlink.project.schedulingservice.exception.RessourceAlreadyExistException;
import medlink.project.schedulingservice.exception.RessourceNotExistException;
import medlink.project.schedulingservice.service.CreneauService;
import medlink.project.schedulingservice.service.RendezVousService;
import org.springframework.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class RendezVousGrpcEndpoint extends RendezVousGrpcServiceGrpc.RendezVousGrpcServiceImplBase {

    private final RendezVousService rendezVousService;
    private final CreneauService creneauService;

    @Override
    public void createRendezVous(CreateRendezVousRequest request,
                                 StreamObserver<RendezVousResponse> observer) {
        execute(observer, () -> {
            validateCreate(request);
            CreneauDto creneau = creneauService.getById(request.getCreneauId());
            if (!creneau.getPraticienId().equals(request.getPraticienId())
                    || !creneau.getCliniqueId().equals(request.getCliniqueId())) {
                throw Status.INVALID_ARGUMENT
                        .withDescription("Le créneau ne correspond pas au praticien/clinique")
                        .asRuntimeException();
            }
            RendezVousDto dto = RendezVousDto.builder()
                    .patientId(request.getPatientId())
                    .creneauId(request.getCreneauId())
                    .type(TypeRendezVous.CONSULTATION)
                    .build();
            return toResponse(rendezVousService.reserver(dto));
        });
    }

    @Override
    public void getRendezVous(GetRendezVousRequest request,
                              StreamObserver<RendezVousResponse> observer) {
        execute(observer, () -> toResponse(rendezVousService.getById(uuid(request.getRendezVousId()))));
    }

    @Override
    public void cancelRendezVous(CancelRendezVousRequest request,
                                 StreamObserver<RendezVousResponse> observer) {
        execute(observer, () -> {
            requireText(request.getIdempotencyKey(), "idempotencyKey");
            return toResponse(rendezVousService.annuler(uuid(request.getRendezVousId())));
        });
    }

    @Override
    public void rescheduleRendezVous(RescheduleRendezVousRequest request,
                                     StreamObserver<RendezVousResponse> observer) {
        execute(observer, () -> {
            requireText(request.getIdempotencyKey(), "idempotencyKey");
            return toResponse(rendezVousService.reporter(uuid(request.getRendezVousId()),
                    positive(request.getNewCreneauId(), "newCreneauId")));
        });
    }

    @Override
    public void updatePresence(UpdatePresenceRequest request,
                               StreamObserver<RendezVousResponse> observer) {
        execute(observer, () -> {
            requireText(request.getIdempotencyKey(), "idempotencyKey");
            UUID id = uuid(request.getRendezVousId());
            return toResponse(request.getPatientPresent()
                    ? rendezVousService.getById(id)
                    : rendezVousService.marquerAbsent(id));
        });
    }

    @Override
    public void completeConsultation(CompleteConsultationRequest request,
                                     StreamObserver<RendezVousResponse> observer) {
        execute(observer, () -> {
            requireText(request.getIdempotencyKey(), "idempotencyKey");
            return toResponse(rendezVousService.terminer(uuid(request.getRendezVousId())));
        });
    }

    @Override
    public void createFollowUpRendezVous(CreateFollowUpRendezVousRequest request,
                                         StreamObserver<RendezVousResponse> observer) {
        execute(observer, () -> {
            requireText(request.getIdempotencyKey(), "idempotencyKey");
            RendezVousDto parent = rendezVousService.getById(uuid(request.getParentRendezVousId()));
            RendezVousDto followUp = RendezVousDto.builder()
                    .patientId(parent.getPatientId())
                    .creneauId(positive(request.getCreneauId(), "creneauId"))
                    .type(TypeRendezVous.CONTROLE)
                    .build();
            return toResponse(rendezVousService.reserver(followUp));
        });
    }

    private RendezVousResponse toResponse(RendezVousDto dto) {
        CreneauDto creneau = creneauService.getById(dto.getCreneauId());
        RendezVousResponse.Builder response = RendezVousResponse.newBuilder()
                .setId(dto.getId().toString())
                .setPatientId(dto.getPatientId())
                .setPraticienId(creneau.getPraticienId())
                .setCliniqueId(creneau.getCliniqueId())
                .setCreneauId(creneau.getId())
                .setStatus(toStatus(dto.getStatus()))
                .setStartAt(toTimestamp(creneau.getDateDebut()))
                .setEndAt(toTimestamp(creneau.getDateFin()));
        if (dto.getStatus() == StatusRendezVous.ABSENT) response.setPatientPresent(false);
        if (dto.getStatus() == StatusRendezVous.TERMINE) response.setPatientPresent(true);
        return response.build();
    }

    private void validateCreate(CreateRendezVousRequest request) {
        positive(request.getPatientId(), "patientId");
        positive(request.getPraticienId(), "praticienId");
        positive(request.getCliniqueId(), "cliniqueId");
        positive(request.getCreneauId(), "creneauId");
        requireText(request.getIdempotencyKey(), "idempotencyKey");
    }

    private RendezVousStatus toStatus(StatusRendezVous status) {
        return switch (status) {
            case EN_ATTENTE, CONFIRME -> RendezVousStatus.RDV_SCHEDULED;
            case ANNULE -> RendezVousStatus.RDV_CANCELLED;
            case TERMINE -> RendezVousStatus.RDV_COMPLETED;
            case ABSENT -> RendezVousStatus.RDV_NO_SHOW;
        };
    }

    private Timestamp toTimestamp(LocalDateTime dateTime) {
        var instant = dateTime.toInstant(ZoneOffset.UTC);
        return Timestamp.newBuilder().setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano()).build();
    }

    private UUID uuid(String value) {
        try { return UUID.fromString(value); }
        catch (RuntimeException exception) {
            throw Status.INVALID_ARGUMENT.withDescription("rendezVousId invalide").asRuntimeException();
        }
    }

    private long positive(long value, String name) {
        if (value <= 0)
            throw Status.INVALID_ARGUMENT.withDescription(name + " invalide").asRuntimeException();
        return value;
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
        } catch (RessourceAlreadyExistException exception) {
            observer.onError(Status.ALREADY_EXISTS.withDescription(exception.getMessage())
                    .asRuntimeException());
        } catch (RessourceNotExistException exception) {
            observer.onError(Status.NOT_FOUND.withDescription(exception.getMessage())
                    .asRuntimeException());
        } catch (BusinessException exception) {
            observer.onError(Status.FAILED_PRECONDITION.withDescription(exception.getMessage())
                    .asRuntimeException());
        } catch (Exception exception) {
            observer.onError(Status.INTERNAL.withDescription(exception.getMessage())
                    .withCause(exception).asRuntimeException());
        }
    }

    @FunctionalInterface
    private interface Operation<T> { T run(); }
}
