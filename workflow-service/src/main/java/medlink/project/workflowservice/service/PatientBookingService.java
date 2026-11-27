package medlink.project.workflowservice.service;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import medlink.grpc.creaneau.v1.CreneauResponse;
import medlink.grpc.creaneau.v1.CreneauStatus;
import medlink.grpc.praticien.v1.PraticienGrpcResponse;
import medlink.grpc.rendezvous.v1.RendezVousResponse;
import medlink.grpc.rendezvous.v1.RendezVousStatus;
import medlink.project.workflowservice.client.CreneauGrpcClient;
import medlink.project.workflowservice.client.PraticienGrpcClient;
import medlink.project.workflowservice.client.RendezVousGrpcClient;
import medlink.project.workflowservice.exception.WorkflowStateException;
import medlink.project.workflowservice.service.WorkflowService.WorkflowInstance;
import medlink.project.workflowservice.service.WorkflowService.WorkflowStatus;
import medlink.project.workflowservice.service.WorkflowService.WorkflowTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientBookingService {

    private static final String SLOT_SELECTION_TASK =
            "sid-09060B9A-FBC3-487C-8CA0-ADD7F9A2D40A";

    private final WorkflowService workflowService;
    private final CreneauGrpcClient creneauGrpcClient;
    private final PraticienGrpcClient praticienGrpcClient;
    private final RendezVousGrpcClient rendezVousGrpcClient;
    private final DirectoryAssignmentClient assignmentClient;

    @Transactional
    public WorkflowStatus book(
            long patientId,
            long slotId,
            String reason,
            String actorUserId,
            String idempotencyKey) {
        try {
            String businessKey = "patient:" + patientId + ":" + idempotencyKey.trim();
            WorkflowInstance existing = workflowService.findByBusinessKey(businessKey).orElse(null);
            if (existing != null) {
                return workflowService.getStatus(existing.processInstanceId());
            }

            CreneauResponse slot = creneauGrpcClient.getById(slotId);
            PraticienGrpcResponse praticien = praticienGrpcClient.getById(slot.getPraticienId());
            if (!praticien.getActif()) {
                throw new WorkflowStateException("Le praticien sélectionné est inactif.");
            }
            assignmentClient.requireActiveAssignment(
                    slot.getPraticienId(), slot.getCliniqueId());

            WorkflowInstance instance = workflowService.start(
                    patientId,
                    slot.getPraticienId(),
                    slot.getCliniqueId(),
                    actorUserId,
                    businessKey
            );

            List<WorkflowTask> tasks = workflowService.getTasks(instance.processInstanceId());
            WorkflowTask selection = tasks.stream()
                    .filter(task -> SLOT_SELECTION_TASK.equals(task.definitionKey()))
                    .findFirst()
                    .orElse(null);
            if (selection != null) {
                if (slot.getStatus() != CreneauStatus.CRENEAU_AVAILABLE
                        || !creneauGrpcClient.isAvailable(slotId)) {
                    throw new WorkflowStateException("Le créneau n'est pas disponible.");
                }
                return workflowService.completeTask(
                        instance.processInstanceId(),
                        selection.id(),
                        Map.of(
                                "creneauId", slotId,
                                "motif", reason == null ? "" : reason.trim()
                        )
                );
            }
            return workflowService.getStatus(instance.processInstanceId());
        } catch (WorkflowStateException exception) {
            throw exception;
        } catch (StatusRuntimeException exception) {
            String detail = exception.getStatus().getDescription();
            throw new WorkflowStateException(detail == null || detail.isBlank()
                    ? "La réservation du créneau a échoué."
                    : detail);
        } catch (RuntimeException exception) {
            throw translateGrpcFailure(
                    exception,
                    "La réservation du créneau a échoué."
            );
        }
    }

    @Transactional
    public WorkflowStatus cancel(
            long patientId,
            String rendezVousId,
            String reason
    ) {
        try {
            RendezVousResponse rendezVous = requireOwnedModifiableAppointment(
                    patientId,
                    rendezVousId
            );
            WorkflowInstance workflow = requireActiveWorkflow(rendezVousId);
            workflowService.cancel(
                    workflow.processInstanceId(),
                    rendezVous.getId(),
                    reason == null ? "" : reason.trim(),
                    UUID.randomUUID().toString()
            );
            return new WorkflowStatus(
                    workflow.processInstanceId(),
                    false,
                    "",
                    rendezVous.getId()
            );
        } catch (WorkflowStateException exception) {
            throw exception;
        } catch (StatusRuntimeException exception) {
            throw grpcFailure(exception, "L'annulation du rendez-vous a échoué.");
        } catch (RuntimeException exception) {
            throw translateGrpcFailure(exception, "L'annulation du rendez-vous a échoué.");
        }
    }

    @Transactional
    public WorkflowStatus reschedule(
            long patientId,
            String rendezVousId,
            long newSlotId
    ) {
        try {
            RendezVousResponse rendezVous = requireOwnedModifiableAppointment(
                    patientId,
                    rendezVousId
            );
            CreneauResponse slot = creneauGrpcClient.getById(newSlotId);
            if (slot.getPraticienId() != rendezVous.getPraticienId()
                    || slot.getCliniqueId() != rendezVous.getCliniqueId()) {
                throw new WorkflowStateException(
                        "Le nouveau créneau doit appartenir au même praticien et à la même clinique."
                );
            }
            if (slot.getStatus() != CreneauStatus.CRENEAU_AVAILABLE
                    || !creneauGrpcClient.isAvailable(newSlotId)
                    || !timestamp(slot.getStartAt().getSeconds(), slot.getStartAt().getNanos())
                    .isAfter(Instant.now())) {
                throw new WorkflowStateException("Le créneau n'est pas disponible.");
            }

            WorkflowInstance workflow = requireActiveWorkflow(rendezVousId);
            workflowService.reschedule(
                    workflow.processInstanceId(),
                    rendezVousId,
                    UUID.randomUUID().toString()
            );
            WorkflowTask selection = workflowService
                    .getTasks(workflow.processInstanceId())
                    .stream()
                    .filter(task -> SLOT_SELECTION_TASK.equals(task.definitionKey()))
                    .findFirst()
                    .orElseThrow(() -> new WorkflowStateException(
                            "La sélection d'un nouveau créneau n'est pas disponible."
                    ));
            return workflowService.completeTask(
                    workflow.processInstanceId(),
                    selection.id(),
                    Map.of("creneauId", newSlotId)
            );
        } catch (WorkflowStateException exception) {
            throw exception;
        } catch (StatusRuntimeException exception) {
            throw grpcFailure(exception, "Le report du rendez-vous a échoué.");
        } catch (RuntimeException exception) {
            throw translateGrpcFailure(exception, "Le report du rendez-vous a échoué.");
        }
    }

    private RendezVousResponse requireOwnedModifiableAppointment(
            long patientId,
            String rendezVousId
    ) {
        RendezVousResponse rendezVous = rendezVousGrpcClient.getById(rendezVousId);
        if (rendezVous.getPatientId() != patientId) {
            throw new WorkflowStateException("Ce rendez-vous n'appartient pas au patient connecté.");
        }
        if (rendezVous.getStatus() != RendezVousStatus.RDV_SCHEDULED) {
            throw new WorkflowStateException("Le rendez-vous est déjà clôturé.");
        }
        if (!timestamp(
                rendezVous.getStartAt().getSeconds(),
                rendezVous.getStartAt().getNanos()
        ).isAfter(Instant.now())) {
            throw new WorkflowStateException(
                    "Un rendez-vous déjà commencé ne peut plus être modifié ou annulé."
            );
        }
        return rendezVous;
    }

    private WorkflowInstance requireActiveWorkflow(String rendezVousId) {
        return workflowService.findActiveByRendezVousId(rendezVousId)
                .orElseThrow(() -> new WorkflowStateException(
                        "Aucun workflow actif ne correspond à ce rendez-vous."
                ));
    }

    private WorkflowStateException grpcFailure(
            StatusRuntimeException exception,
            String fallback
    ) {
        String detail = exception.getStatus().getDescription();
        return new WorkflowStateException(
                detail == null || detail.isBlank() ? fallback : detail
        );
    }

    private RuntimeException translateGrpcFailure(
            RuntimeException exception,
            String fallback
    ) {
        Throwable cause = exception;
        while (cause != null) {
            if (cause instanceof StatusRuntimeException statusException) {
                return grpcFailure(statusException, fallback);
            }
            cause = cause.getCause();
        }
        return exception;
    }

    private Instant timestamp(long seconds, int nanos) {
        return Instant.ofEpochSecond(seconds, nanos);
    }
}
