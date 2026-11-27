package medlink.project.medicalrecordservice.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtAuthConverterTest {

    private final JwtAuthConverter converter = new JwtAuthConverter("medical-record-service");

    @Test
    void extractsWorkflowServiceClientRole() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("workflow-service")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60))
                .claim("resource_access", Map.of(
                        "medical-record-service",
                        Map.of("roles", List.of("WORKFLOW_SERVICE"))
                ))
                .build();

        var authentication = converter.convert(jwt);

        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_WORKFLOW_SERVICE")));
    }
}
