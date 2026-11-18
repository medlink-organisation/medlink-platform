package medlink.project.medicalrecordservice.config.properties;

import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "medical.files")
public record MedicalFileProperties(
        @Positive long maxPdfSizeBytes
) {
}
