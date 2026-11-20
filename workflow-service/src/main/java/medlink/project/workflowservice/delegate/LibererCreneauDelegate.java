package medlink.project.workflowservice.delegate;

import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.client.RendezVousGrpcClient;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("libererCreneauDelegate")
@RequiredArgsConstructor
public class LibererCreneauDelegate implements JavaDelegate {

    private final RendezVousGrpcClient rendezVousGrpcClient;

    @Override
    public void execute(DelegateExecution execution) {
        rendezVousGrpcClient.cancel(
                requiredString(execution, "rdvId"),
                stringValue(execution, "annulationMotif", "Annulation du rendez-vous"),
                operationId(execution)
        );
    }

    private String requiredString(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value instanceof String string && !string.isBlank()) {
            return string;
        }
        throw new IllegalArgumentException(name + " obligatoire");
    }

    private String stringValue(
            DelegateExecution execution,
            String name,
            String defaultValue) {

        Object value = execution.getVariable(name);
        return value instanceof String string && !string.isBlank()
                ? string
                : defaultValue;
    }

    private String operationId(DelegateExecution execution) {
        return execution.getProcessInstanceId()
                + ":" + execution.getCurrentActivityId()
                + ":cancel";
    }
}
