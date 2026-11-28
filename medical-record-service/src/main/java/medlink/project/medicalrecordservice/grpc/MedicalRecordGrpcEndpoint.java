package medlink.project.medicalrecordservice.grpc;

import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import medlink.grpc.medical.v1.ConsultationReportResponse;
import medlink.grpc.medical.v1.EnsureMedicalRecordRequest;
import medlink.grpc.medical.v1.MedicalRecordGrpcServiceGrpc;
import medlink.grpc.medical.v1.MedicalRecordResponse;
import medlink.grpc.medical.v1.RegularizeCompletedAppointmentRequest;
import medlink.grpc.medical.v1.SaveConsultationReportRequest;
import medlink.grpc.rendezvous.v1.GetRendezVousRequest;
import medlink.grpc.rendezvous.v1.RendezVousGrpcServiceGrpc;
import medlink.grpc.rendezvous.v1.RendezVousStatus;
import medlink.project.medicalrecordservice.dto.DossierMedicalDto;
import medlink.project.medicalrecordservice.service.InternalMedicalRecordService;
import org.springframework.grpc.server.service.GrpcService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@GrpcService
@RequiredArgsConstructor
public class MedicalRecordGrpcEndpoint extends MedicalRecordGrpcServiceGrpc.MedicalRecordGrpcServiceImplBase {

    private final InternalMedicalRecordService service;
    private final RendezVousGrpcServiceGrpc.RendezVousGrpcServiceBlockingStub rendezVousStub;

    @Override
    public void ensureMedicalRecord(
            EnsureMedicalRecordRequest request,
            StreamObserver<MedicalRecordResponse> observer
    ) {
        execute(observer, () -> toResponse(service.ensureDossierForPatient(request.getPatientId())));
    }

    @Override
    public void saveConsultationReport(
            SaveConsultationReportRequest request,
            StreamObserver<ConsultationReportResponse> observer
    ) {
        execute(observer, () -> {
            var report = service.saveCompteRendu(new InternalMedicalRecordService.ConsultationReportCommand(
                    request.getRecordId(),
                    UUID.fromString(request.getAppointmentId()),
                    request.getPractitionerId(),
                    request.getClinicId(),
                    toLocalDateTime(request.getConsultationAt()),
                    request.getReason(),
                    request.getSymptoms(),
                    request.getDiagnosis(),
                    request.getObservations(),
                    request.getRecommendedTreatment(),
                    request.getAdditionalExaminations(),
                    request.getRecommendations(),
                    request.getFollowUpRequired()
            ));
            return ConsultationReportResponse.newBuilder()
                    .setReportId(report.getId().toString())
                    .setRecordId(report.getDossierMedicalId())
                    .setAppointmentId(report.getRendezVousId().toString())
                    .setRecordedAt(toTimestamp(report.getDateRedaction()))
                    .build();
        });
    }

    @Override
    public void regularizeCompletedAppointment(
            RegularizeCompletedAppointmentRequest request,
            StreamObserver<MedicalRecordResponse> observer
    ) {
        execute(observer, () -> {
            var rendezVous = rendezVousStub.withDeadlineAfter(5, TimeUnit.SECONDS)
                    .getRendezVous(GetRendezVousRequest.newBuilder()
                            .setRendezVousId(request.getAppointmentId())
                            .build());
            if (rendezVous.getStatus() != RendezVousStatus.RDV_COMPLETED) {
                throw Status.FAILED_PRECONDITION
                        .withDescription("Le rendez-vous n'est pas terminé.")
                        .asRuntimeException();
            }
            return toResponse(service.ensureDossierForPatient(rendezVous.getPatientId()));
        });
    }

    private MedicalRecordResponse toResponse(DossierMedicalDto dossier) {
        return MedicalRecordResponse.newBuilder()
                .setRecordId(dossier.getId())
                .setPatientId(dossier.getPatientId())
                .setCreatedAt(toTimestamp(dossier.getDateCreation()))
                .setLastModifiedAt(toTimestamp(dossier.getDerniereModification()))
                .build();
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos())
                        .truncatedTo(ChronoUnit.MICROS),
                ZoneOffset.UTC
        );
    }

    private Timestamp toTimestamp(LocalDateTime value) {
        Instant instant = value.toInstant(ZoneOffset.UTC);
        return Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).setNanos(instant.getNano()).build();
    }

    private <T> void execute(StreamObserver<T> observer, java.util.concurrent.Callable<T> action) {
        try {
            observer.onNext(action.call());
            observer.onCompleted();
        } catch (io.grpc.StatusRuntimeException exception) {
            observer.onError(exception);
        } catch (IllegalArgumentException exception) {
            observer.onError(Status.INVALID_ARGUMENT.withDescription(exception.getMessage()).asRuntimeException());
        } catch (Exception exception) {
            observer.onError(Status.FAILED_PRECONDITION.withDescription(exception.getMessage()).asRuntimeException());
        }
    }
}
