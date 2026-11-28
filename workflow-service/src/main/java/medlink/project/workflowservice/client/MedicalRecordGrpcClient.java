package medlink.project.workflowservice.client;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import medlink.grpc.medical.v1.ConsultationReportResponse;
import medlink.grpc.medical.v1.EnsureMedicalRecordRequest;
import medlink.grpc.medical.v1.MedicalRecordGrpcServiceGrpc;
import medlink.grpc.medical.v1.MedicalRecordResponse;
import medlink.grpc.medical.v1.SaveConsultationReportRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MedicalRecordGrpcClient {

    private final MedicalRecordGrpcServiceGrpc.MedicalRecordGrpcServiceBlockingStub stub;

    public MedicalRecordResponse ensureForPatient(long patientId, String idempotencyKey) {
        return client().ensureMedicalRecord(EnsureMedicalRecordRequest.newBuilder()
                .setPatientId(patientId)
                .setIdempotencyKey(idempotencyKey)
                .build());
    }

    public ConsultationReportResponse saveReport(
            long recordId,
            String appointmentId,
            long practitionerId,
            long clinicId,
            Instant consultationAt,
            String reason,
            String symptoms,
            String diagnosis,
            String observations,
            String treatment,
            String examinations,
            String recommendations,
            boolean followUpRequired,
            String idempotencyKey
    ) {
        return client().saveConsultationReport(SaveConsultationReportRequest.newBuilder()
                .setRecordId(recordId)
                .setAppointmentId(appointmentId)
                .setPractitionerId(practitionerId)
                .setClinicId(clinicId)
                .setConsultationAt(timestamp(consultationAt))
                .setReason(reason)
                .setSymptoms(symptoms)
                .setDiagnosis(diagnosis)
                .setObservations(observations)
                .setRecommendedTreatment(treatment)
                .setAdditionalExaminations(examinations)
                .setRecommendations(recommendations)
                .setFollowUpRequired(followUpRequired)
                .setIdempotencyKey(idempotencyKey)
                .build());
    }

    private MedicalRecordGrpcServiceGrpc.MedicalRecordGrpcServiceBlockingStub client() {
        return stub.withDeadlineAfter(8, TimeUnit.SECONDS);
    }

    private Timestamp timestamp(Instant value) {
        return Timestamp.newBuilder().setSeconds(value.getEpochSecond()).setNanos(value.getNano()).build();
    }
}
