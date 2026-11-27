package medlink.project.medicalrecordservice;

import medlink.project.auditcore.revision.AuditRevisionEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@EntityScan(basePackageClasses = {MedicalRecordServiceApplication.class, AuditRevisionEntity.class})
public class MedicalRecordServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalRecordServiceApplication.class, args);
    }

}
