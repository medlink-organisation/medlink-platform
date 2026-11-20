package medlink.project.workflowservice.delegate;

import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.client.RendezVousGrpcClient;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("finaliserConsultationDelegate")
@RequiredArgsConstructor
public class FinaliserConsultationDelegate implements JavaDelegate {

    private final RendezVousGrpcClient rendezVousGrpcClient;

    @Override
    public void execute(DelegateExecution execution) {
        String rendezVousId = requiredString(execution, "rdvId");
        boolean controleRequis = requiredBoolean(execution, "controleRequis");
        String operationPrefix = execution.getProcessInstanceId()
                + ":" + execution.getCurrentActivityId();

        rendezVousGrpcClient.updatePresence(
                rendezVousId,
                true,
                operationPrefix + ":presence"
        );
        rendezVousGrpcClient.completeConsultation(
                rendezVousId,
                controleRequis,
                operationPrefix + ":complete"
        );
        execution.setVariable("patientPresent", true);
    }

    private String requiredString(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value instanceof String string && !string.isBlank()) {
            return string;
        }
        throw new IllegalArgumentException(name + " obligatoire");
    }

    private boolean requiredBoolean(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value instanceof Boolean bool) {
            return bool;
        }
        throw new IllegalArgumentException(name + " obligatoire");
    }
}
