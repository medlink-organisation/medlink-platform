package medlink.project.workflowservice.delegate;

import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.client.RendezVousGrpcClient;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("marquerAbsentDelegate")
@RequiredArgsConstructor
public class MarquerAbsentDelegate implements JavaDelegate {

    private final RendezVousGrpcClient rendezVousGrpcClient;

    @Override
    public void execute(DelegateExecution execution) {
        Object rdvId = execution.getVariable("rdvId");
        if (!(rdvId instanceof String id) || id.isBlank()) {
            throw new IllegalArgumentException("rdvId obligatoire");
        }

        rendezVousGrpcClient.updatePresence(
                id,
                false,
                execution.getProcessInstanceId()
                        + ":" + execution.getCurrentActivityId()
                        + ":absence"
        );
        execution.setVariable("patientPresent", false);
    }
}
