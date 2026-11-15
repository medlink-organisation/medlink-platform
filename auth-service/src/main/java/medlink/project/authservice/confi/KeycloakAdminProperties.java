package medlink.project.authservice.confi;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakAdminProperties {

    @NotBlank
    private String serverUrl;

    @NotBlank
    private String realm;

    @NotBlank
    private String clientId;

    @NotBlank
    private String clientSecret;

    public String getTokenUri() {
        return serverUrl + "/realms/" + realm
                + "/protocol/openid-connect/token";
    }
}