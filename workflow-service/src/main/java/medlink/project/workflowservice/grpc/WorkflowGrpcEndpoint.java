package medlink.project.workflowservice.grpc;

import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest;
import medlink.grpc.workflow.v1.GetWorkflowStatusRequest;
import medlink.grpc.workflow.v1.GetWorkflowStatusResponse;
import medlink.grpc.workflow.v1.RequestRescheduleRequest;
import medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest;
import medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse;
import medlink.grpc.workflow.v1.WorkflowEventResponse;
import medlink.grpc.workflow.v1.WorkflowGrpcServiceGrpc;
import medlink.project.workflowservice.exception.WorkflowNotFoundException;
import medlink.project.workflowservice.exception.WorkflowStateException;
import medlink.project.workflowservice.exception.WorkflowTaskNotFoundException;
import medlink.project.workflowservice.service.WorkflowService;
import org.springframework.grpc.server.service.GrpcService;

import java.time.Instant;

@GrpcService
@RequiredArgsConstructor
public class WorkflowGrpcEndpoint
        extends WorkflowGrpcServiceGrpc.WorkflowGrpcServiceImplBase {

    private final WorkflowService workflowService;

    @Override
    public void startAppointmentWorkflow(
            StartAppointmentWorkflowRequest request,
            StreamObserver<StartAppointmentWorkflowResponse> observer) {

        execute(observer, () -> {
            WorkflowService.WorkflowInstance workflow = workflowService.start(
                    request.getPatientId(),
                    request.getPraticienId(),
                    request.getCliniqueId(),
                    request.getActorUserId(),
                    request.getIdempotencyKey()
            );
            return StartAppointmentWorkflowResponse.newBuilder()
                    .setProcessInstanceId(workflow.processInstanceId())
                    .setBusinessKey(workflow.businessKey())
                    .build();
        });
    }

    @Override
    public void getWorkflowStatus(
            GetWorkflowStatusRequest request,
            StreamObserver<GetWorkflowStatusResponse> observer) {

        execute(observer, () -> {
            WorkflowService.WorkflowStatus status = workflowService.getStatus(
                    request.getProcessInstanceId());
            return GetWorkflowStatusResponse.newBuilder()
                    .setProcessInstanceId(status.processInstanceId())
                    .setActive(status.active())
                    .setCurrentActivityId(status.currentActivityId())
                    .setRendezVousId(status.rendezVousId())
                    .build();
        });
    }

    @Override
    public void cancelAppointmentWorkflow(
            CancelAppointmentWorkflowRequest request,
            StreamObserver<WorkflowEventResponse> observer) {

        execute(observer, () -> toResponse(workflowService.cancel(
                request.getProcessInstanceId(),
                request.getRendezVousId(),
                request.getReason(),
                request.getEventId()
        )));
    }

    @Override
    public void requestReschedule(
            RequestRescheduleRequest request,
            StreamObserver<WorkflowEventResponse> observer) {

        execute(observer, () -> toResponse(workflowService.reschedule(
                request.getProcessInstanceId(),
                request.getRendezVousId(),
                request.getEventId()
        )));
    }

    private WorkflowEventResponse toResponse(WorkflowService.WorkflowEvent event) {
        return WorkflowEventResponse.newBuilder()
                .setAccepted(event.accepted())
                .setProcessInstanceId(event.processInstanceId())
                .setReceivedAt(toTimestamp(event.receivedAt()))
                .build();
    }

    private Timestamp toTimestamp(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    private <T> void execute(StreamObserver<T> observer, Operation<T> operation) {
        try {
            observer.onNext(operation.run());
            observer.onCompleted();
        } catch (IllegalArgumentException exception) {
            observer.onError(Status.INVALID_ARGUMENT
                    .withDescription(exception.getMessage())
                    .asRuntimeException());
        } catch (WorkflowNotFoundException | WorkflowTaskNotFoundException exception) {
            observer.onError(Status.NOT_FOUND
                    .withDescription(exception.getMessage())
                    .asRuntimeException());
        } catch (WorkflowStateException exception) {
            observer.onError(Status.FAILED_PRECONDITION
                    .withDescription(exception.getMessage())
                    .asRuntimeException());
        } catch (Exception exception) {
            observer.onError(Status.INTERNAL
                    .withDescription(exception.getMessage())
                    .withCause(exception)
                    .asRuntimeException());
        }
    }

    @FunctionalInterface
    private interface Operation<T> {
        T run();
    }
}
