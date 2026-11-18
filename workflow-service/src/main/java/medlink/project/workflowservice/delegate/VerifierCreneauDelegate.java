package medlink.project.workflowservice.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("verifierCrenauDelegate")
public class VerifierCreneauDelegate implements JavaDelegate {
    private final RendezVousGateway rendezVousGateway ;
    @Override
    public void execute(DelegateExecution execution) {
        Long crenauId = (Long) execution.getVariable("crenauId");
        if (crenauId == null ) {
            throw  new  IllegalArgumentException( "crenauId obligatoire");
        }

        boolean libre =
                rendezVousGateway.isCreneauLibre(creneauId);

        execution.setVariable("creneauLibre", libre);
    }
}
