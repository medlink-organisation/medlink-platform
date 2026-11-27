package medlink.project.workflowservice.service;

import medlink.grpc.creaneau.v1.CreneauResponse;
import medlink.grpc.creaneau.v1.CreneauStatus;
import medlink.grpc.praticien.v1.PraticienGrpcResponse;
import medlink.project.workflowservice.client.CreneauGrpcClient;
import medlink.project.workflowservice.client.PraticienGrpcClient;
import medlink.project.workflowservice.service.WorkflowService.WorkflowInstance;
import medlink.project.workflowservice.service.WorkflowService.WorkflowStatus;
import medlink.project.workflowservice.service.WorkflowService.WorkflowTask;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PatientBookingServiceTest {

    private final WorkflowService workflowService = mock(WorkflowService.class);
    private final CreneauGrpcClient creneauClient = mock(CreneauGrpcClient.class);
    private final PraticienGrpcClient praticienClient = mock(PraticienGrpcClient.class);
    private final DirectoryAssignmentClient assignmentClient = mock(DirectoryAssignmentClient.class);
    private final PatientBookingService service = new PatientBookingService(
            workflowService, creneauClient, praticienClient, assignmentClient);

    @Test
    void completesSlotSelectionForANewBooking() {
        when(workflowService.findByBusinessKey("patient:42:key-1"))
                .thenReturn(Optional.empty());
        when(creneauClient.getById(7L)).thenReturn(slot(CreneauStatus.CRENEAU_AVAILABLE));
        when(creneauClient.isAvailable(7L)).thenReturn(true);
        when(praticienClient.getById(2L)).thenReturn(activePraticien());
        when(workflowService.start(42L, 2L, 3L, "kc-patient", "patient:42:key-1"))
                .thenReturn(new WorkflowInstance("process-1", "patient:42:key-1"));
        when(workflowService.getTasks("process-1")).thenReturn(List.of(
                new WorkflowTask("task-1", "sid-09060B9A-FBC3-487C-8CA0-ADD7F9A2D40A",
                        "Choisir", "kc-patient", Instant.now())));
        WorkflowStatus confirmed = new WorkflowStatus("process-1", true, "attendance", "rdv-1");
        when(workflowService.completeTask("process-1", "task-1", java.util.Map.of(
                "creneauId", 7L, "motif", "Consultation"))).thenReturn(confirmed);

        assertThat(service.book(42L, 7L, "Consultation", "kc-patient", "key-1"))
                .isEqualTo(confirmed);
    }

    @Test
    void returnsExistingBookingOnIdempotentRetryEvenWhenSlotIsReserved() {
        when(workflowService.findByBusinessKey("patient:42:key-1"))
                .thenReturn(Optional.of(new WorkflowInstance("process-1", "patient:42:key-1")));
        WorkflowStatus existing = new WorkflowStatus("process-1", true, "attendance-task", "rdv-1");
        when(workflowService.getStatus("process-1")).thenReturn(existing);

        assertThat(service.book(42L, 7L, "Consultation", "kc-patient", "key-1"))
                .isEqualTo(existing);
        verify(creneauClient, never()).getById(7L);
        verify(creneauClient, never()).isAvailable(7L);
        verify(workflowService, never()).completeTask(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(), anyMap());
    }

    private CreneauResponse slot(CreneauStatus status) {
        return CreneauResponse.newBuilder()
                .setId(7L).setPraticienId(2L).setCliniqueId(3L).setStatus(status).build();
    }

    private PraticienGrpcResponse activePraticien() {
        return PraticienGrpcResponse.newBuilder().setId(2L).setActif(true).build();
    }
}
