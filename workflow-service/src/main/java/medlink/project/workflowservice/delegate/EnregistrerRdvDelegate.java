package medlink.project.workflowservice.delegate;

import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.client.RendezVousGateway;
import medlink.project.workflowservice.client.RendezVousGateway.RendezVousInfo;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component("enregistrerRdvDelegate")
@RequiredArgsConstructor
public class EnregistrerRdvDelegate implements JavaDelegate {

    private final RendezVousGateway rendezVousGateway;

    @Override
    public void execute(DelegateExecution execution) {

        String typeRdv =
                (String) execution.getVariable("typeRdv");

        Long patientId =
                (Long) execution.getVariable("patientId");

        Long praticienMetierId =
                (Long) execution.getVariable("praticienMetierId");

        // Clé stable pendant les retries d'une même réservation
        String operationId =
                execution.getProcessInstanceId()
                        + ":"
                        + execution.getCurrentActivityId()
                        + ":"
                        + execution.getVariable("reservationSequence");

        RendezVousInfo rdv;

        if ("CONTROLE".equals(typeRdv)) {

            Long parentRdvId =
                    (Long) execution.getVariable("parentRdvId");

            // Préparée par le backend lors de la User Task
            OffsetDateTime dateControle =
                    (OffsetDateTime) execution.getVariable(
                            "dateControleUtc"
                    );

            rdv = rendezVousGateway.reserverRdvControle(
                    operationId,
                    patientId,
                    praticienMetierId,
                    parentRdvId,
                    dateControle
            );

        } else if ("NORMAL".equals(typeRdv)) {

            Long creneauId =
                    (Long) execution.getVariable("creneauId");

            rdv = rendezVousGateway.reserverRdvNormal(
                    operationId,
                    patientId,
                    praticienMetierId,
                    creneauId
            );

        } else {
            throw new IllegalStateException(
                    "Type de RDV invalide : " + typeRdv
            );
        }

        OffsetDateTime date = rdv.dateRdv();

        execution.setVariable("rdvId", rdv.id());

        execution.setVariable(
                "dateRdv",
                Date.from(date.toInstant())
        );

        execution.setVariable(
                "rappelDate",
                Date.from(date.toInstant().minusSeconds(86400))
        );

        execution.setVariable(
                "patientEmail",
                rdv.patientEmail()
        );

        execution.setVariable("patientPresent", null);
        execution.setVariable("controleRequis", null);
    }
}