package medlink.project.schedulingservice;

import medlink.project.auditcore.revision.AuditRevisionEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackageClasses = {SchedulingServiceApplication.class, AuditRevisionEntity.class})
public class SchedulingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulingServiceApplication.class, args);
    }

}
