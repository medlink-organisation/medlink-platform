package medlink.project.workflowservice.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("preparerControleDelegate")
public class PreparerControleDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        Object rendezVousId = execution.getVariable("rdvId");
        if (!(rendezVousId instanceof String id) || id.isBlank()) {
            throw new IllegalArgumentException("rdvId obligatoire");
        }
        Object creneauId = execution.getVariable("creneauId");
        if (!(creneauId instanceof Number number) || number.longValue() <= 0) {
            throw new IllegalArgumentException("creneauId obligatoire");
        }

        execution.setVariable("parentRdvId", id);
        execution.setVariable("typeRdv", "CONTROLE");
        execution.setVariable("creneauId", number.longValue());
    }
}
