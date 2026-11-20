package medlink.project.workflowservice.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("demanderAutreCreneauDelegate")
public class DemanderAutreCreneauDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {

        execution.removeVariable("creneauId");
        execution.removeVariable("creneauLibre");
        execution.setVariable("messageSelection",
                "Créneau indisponible. Choisissez un autre créneau.");
    }
}
