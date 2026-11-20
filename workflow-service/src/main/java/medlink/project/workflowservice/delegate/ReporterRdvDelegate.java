package medlink.project.workflowservice.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("reporterRdvDelegate")
public class ReporterRdvDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        if (!(execution.getVariable("rdvId") instanceof String rdvId)
                || rdvId.isBlank()) {
            throw new IllegalArgumentException("rdvId obligatoire");
        }

        execution.setVariable("reportRdv", true);
        execution.removeVariable("creneauId");
        execution.removeVariable("creneauLibre");
        execution.removeVariable("dateRdv");
        execution.removeVariable("rappelDate");
        execution.setVariable("messageSelection",
                "Choisissez un nouveau créneau pour reporter le rendez-vous.");
    }
}
