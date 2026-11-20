package medlink.project.workflowservice.client;

import lombok.RequiredArgsConstructor;
import medlink.grpc.rendezvous.v1.CancelRendezVousRequest;
import medlink.grpc.rendezvous.v1.CompleteConsultationRequest;
import medlink.grpc.rendezvous.v1.CreateFollowUpRendezVousRequest;
import medlink.grpc.rendezvous.v1.CreateRendezVousRequest;
import medlink.grpc.rendezvous.v1.GetRendezVousRequest;
import medlink.grpc.rendezvous.v1.RendezVousGrpcServiceGrpc;
import medlink.grpc.rendezvous.v1.RendezVousResponse;
import medlink.grpc.rendezvous.v1.RescheduleRendezVousRequest;
import medlink.grpc.rendezvous.v1.UpdatePresenceRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RendezVousGrpcClient {

    private final RendezVousGrpcServiceGrpc
            .RendezVousGrpcServiceBlockingStub stub;

    private RendezVousGrpcServiceGrpc
            .RendezVousGrpcServiceBlockingStub client() {

        return stub.withDeadlineAfter(
                5, TimeUnit.SECONDS);
    }

    public RendezVousResponse create(
            Long patientId,
            Long praticienId,
            Long cliniqueId,
            Long creneauId,
            String motif,
            String idempotencyKey) {

        CreateRendezVousRequest request =
                CreateRendezVousRequest.newBuilder()
                        .setPatientId(patientId)
                        .setPraticienId(praticienId)
                        .setCliniqueId(cliniqueId)
                        .setCreneauId(creneauId)
                        .setMotif(motif)
                        .setIdempotencyKey(idempotencyKey)
                        .build();

        return client().createRendezVous(request);
    }

    public RendezVousResponse getById(String id) {

        GetRendezVousRequest request =
                GetRendezVousRequest.newBuilder()
                        .setRendezVousId(id)
                        .build();

        return client().getRendezVous(request);
    }

    public RendezVousResponse cancel(
            String rendezVousId,
            String reason,
            String idempotencyKey) {

        CancelRendezVousRequest request =
                CancelRendezVousRequest.newBuilder()
                        .setRendezVousId(rendezVousId)
                        .setReason(reason)
                        .setIdempotencyKey(idempotencyKey)
                        .build();

        return client().cancelRendezVous(request);
    }

    public RendezVousResponse reschedule(
            String rendezVousId,
            Long newCreneauId,
            String idempotencyKey) {

        RescheduleRendezVousRequest request =
                RescheduleRendezVousRequest.newBuilder()
                        .setRendezVousId(rendezVousId)
                        .setNewCreneauId(newCreneauId)
                        .setIdempotencyKey(idempotencyKey)
                        .build();

        return client().rescheduleRendezVous(request);
    }

    public RendezVousResponse updatePresence(
            String rendezVousId,
            boolean patientPresent,
            String idempotencyKey) {

        UpdatePresenceRequest request =
                UpdatePresenceRequest.newBuilder()
                        .setRendezVousId(rendezVousId)
                        .setPatientPresent(patientPresent)
                        .setIdempotencyKey(idempotencyKey)
                        .build();

        return client().updatePresence(request);
    }

    public RendezVousResponse completeConsultation(
            String rendezVousId,
            boolean controleRequis,
            String idempotencyKey) {

        CompleteConsultationRequest request =
                CompleteConsultationRequest.newBuilder()
                        .setRendezVousId(rendezVousId)
                        .setControleRequis(controleRequis)
                        .setIdempotencyKey(idempotencyKey)
                        .build();

        return client().completeConsultation(request);
    }

    public RendezVousResponse createFollowUp(
            String parentRendezVousId,
            Long creneauId,
            String motif,
            String idempotencyKey) {

        CreateFollowUpRendezVousRequest request =
                CreateFollowUpRendezVousRequest.newBuilder()
                        .setParentRendezVousId(parentRendezVousId)
                        .setCreneauId(creneauId)
                        .setMotifControle(motif)
                        .setIdempotencyKey(idempotencyKey)
                        .build();

        return client().createFollowUpRendezVous(request);
    }
}
