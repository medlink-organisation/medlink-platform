package medlink.project.workflowservice.service;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import medlink.grpc.creaneau.v1.CreneauResponse;
import medlink.grpc.creaneau.v1.CreneauStatus;
import medlink.grpc.praticien.v1.PraticienGrpcResponse;
import medlink.project.workflowservice.client.CreneauGrpcClient;
import medlink.project.workflowservice.client.PraticienGrpcClient;
import medlink.project.workflowservice.exception.WorkflowStateException;
import medlink.project.workflowservice.service.WorkflowService.WorkflowInstance;
import medlink.project.workflowservice.service.WorkflowService.WorkflowStatus;
import medlink.project.workflowservice.service.WorkflowService.WorkflowTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PatientBookingService {

    private static final String SLOT_SELECTION_TASK =
            "sid-09060B9A-FBC3-487C-8CA0-ADD7F9A2D40A";

    private final WorkflowService workflowService;
    private final CreneauGrpcClient creneauGrpcClient;
    private final PraticienGrpcClient praticienGrpcClient;
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
        }
    }
}
