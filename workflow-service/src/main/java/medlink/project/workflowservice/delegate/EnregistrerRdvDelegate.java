package medlink.project.workflowservice.delegate;

import lombok.RequiredArgsConstructor;
import medlink.grpc.rendezvous.v1.RendezVousResponse;
import medlink.project.workflowservice.client.RendezVousGrpcClient;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component("enregistrerRdvDelegate")
@RequiredArgsConstructor
public class EnregistrerRdvDelegate implements JavaDelegate {

    private final RendezVousGrpcClient rendezVousGrpcClient;

    @Override
    public void execute(DelegateExecution execution) {

        String typeRdv = stringVariable(execution, "typeRdv", "NORMAL");
        Long creneauId = requiredLongVariable(execution, "creneauId");
        String operationId = idempotencyKey(execution, typeRdv, creneauId);

        RendezVousResponse rdv;
        if (Boolean.TRUE.equals(execution.getVariable("reportRdv"))) {
            rdv = rendezVousGrpcClient.reschedule(
                    requiredStringVariable(execution, "rdvId"),
                    requiredLongVariable(execution, "patientId"),
                    creneauId,
                    operationId
            );
            execution.removeVariable("reportRdv");
        } else {
            rdv = createRendezVous(execution, typeRdv, creneauId, operationId);
        }

        Instant startAt = Instant.ofEpochSecond(
                rdv.getStartAt().getSeconds(),
                rdv.getStartAt().getNanos()
        );

        execution.setVariable("rdvId", rdv.getId());
        execution.setVariable("dateRdv", Date.from(startAt));
        execution.setVariable("rappelDate", Date.from(startAt.minusSeconds(86_400)));

        execution.setVariable("patientPresent", null);
        execution.setVariable("controleRequis", null);
    }

    private RendezVousResponse createRendezVous(
            DelegateExecution execution,
            String typeRdv,
            Long creneauId,
            String operationId) {

        return switch (typeRdv) {
            case "NORMAL" -> rendezVousGrpcClient.create(
                    requiredLongVariable(execution, "patientId"),
                    praticienId(execution),
                    requiredLongVariable(execution, "cliniqueId"),
                    creneauId,
                    stringVariable(execution, "motif", ""),
                    operationId
            );
            case "CONTROLE" -> rendezVousGrpcClient.createFollowUp(
                    requiredStringVariable(execution, "parentRdvId"),
                    creneauId,
                    stringVariable(execution, "motifControle", ""),
                    operationId
            );
            default -> throw new IllegalStateException(
                    "Type de RDV invalide : " + typeRdv);
        };
    }

    private String idempotencyKey(
            DelegateExecution execution,
            String typeRdv,
            Long creneauId) {

        return String.join(":",
                execution.getProcessInstanceId(),
                execution.getCurrentActivityId(),
                typeRdv,
                String.valueOf(creneauId));
    }

    private Long praticienId(DelegateExecution execution) {
        Long praticienId = longVariable(execution, "praticienId");
        return praticienId != null
                ? praticienId
                : requiredLongVariable(execution, "praticienMetierId");
    }

    private Long requiredLongVariable(DelegateExecution execution, String name) {
        Long value = longVariable(execution, name);
        if (value == null) {
            throw new IllegalArgumentException(name + " obligatoire");
        }
        return value;
    }

    private Long longVariable(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        return value instanceof Number number ? number.longValue() : null;
    }

    private String stringVariable(
            DelegateExecution execution,
            String name,
            String defaultValue) {

        Object value = execution.getVariable(name);
        return value instanceof String string && !string.isBlank()
                ? string
                : defaultValue;
    }

    private String requiredStringVariable(DelegateExecution execution, String name) {
        String value = stringVariable(execution, name, null);
        if (value == null) {
            throw new IllegalArgumentException(name + " obligatoire");
        }
        return value;
    }
}
