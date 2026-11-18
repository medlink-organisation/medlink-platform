package medlink.project.medicalrecordservice.config.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "clamav")
public record ClamAvProperties(
        @NotBlank String host,
        @Min(1) @Max(65535) int port,
        @Positive int timeout
) {
}
