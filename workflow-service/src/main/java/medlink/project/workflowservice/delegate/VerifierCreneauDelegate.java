package medlink.project.workflowservice.delegate;

import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.client.CreneauGrpcClient;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("verifierCreneauDelegate")
@RequiredArgsConstructor
public class VerifierCreneauDelegate implements JavaDelegate {

    private final CreneauGrpcClient creneauGrpcClient;

    @Override
    public void execute(DelegateExecution execution) {
        Long creneauId = longVariable(execution, "creneauId");
        if (creneauId == null) {
            throw new IllegalArgumentException("creneauId obligatoire");
        }

        execution.setVariable(
                "creneauLibre",
                creneauGrpcClient.isAvailable(creneauId)
        );
    }

    private Long longVariable(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        return value instanceof Number number ? number.longValue() : null;
    }
}
