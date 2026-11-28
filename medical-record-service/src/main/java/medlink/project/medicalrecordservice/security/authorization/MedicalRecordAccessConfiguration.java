package medlink.project.medicalrecordservice.security.authorization;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;

import java.util.UUID;

@Configuration
public class MedicalRecordAccessConfiguration {

    @Bean
    @ConditionalOnMissingBean(MedicalRecordAccessService.class)
    MedicalRecordAccessService failClosedMedicalRecordAccessService() {
        return new MedicalRecordAccessService() {
            @Override
            public void checkCanCreateForPatient(Long patientId) {
                deny();
            }

            @Override
            public void checkCanRead(Long dossierId) {
                deny();
            }

            @Override
            public void checkCanWrite(Long dossierId) {
                deny();
            }

            @Override
            public void checkCanAttachResult(Long dossierId, UUID rendezVousId) {
                deny();
            }

            @Override
            public Long requireRecordIdForAppointment(UUID rendezVousId) {
                deny();
                return null;
            }

            @Override
            public void checkCanListAll() {
                deny();
            }

            @Override
            public void checkCanListByPractitioner(Long practitionerId) {
                deny();
            }

            private void deny() {
                throw new AccessDeniedException(
                        "Le contrôle d'accès métier aux dossiers médicaux n'est pas configuré."
                );
            }
        };
    }
}
