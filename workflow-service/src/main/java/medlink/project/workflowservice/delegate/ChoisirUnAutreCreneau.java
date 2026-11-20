package medlink.project.workflowservice.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * Alias explicite utilisable par un processus alternatif au BPMN principal.
 */
@Component("choisirUnAutreCreneau")
public class ChoisirUnAutreCreneau implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        execution.removeVariable("creneauId");
        execution.removeVariable("creneauLibre");
        execution.setVariable("messageSelection",
                "Créneau indisponible. Choisissez un autre créneau.");
    }
}
