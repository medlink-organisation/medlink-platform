package medlink.project.medicalrecordservice.audit;

import medlink.project.medicalrecordservice.security.JwtAuthConverter;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtAuthConverterTest {

    @Test
    void exposesKeycloakAuditRolesAsDedicatedAuthorities() {
        Instant now = Instant.now();
        Jwt jwt = new Jwt(
                "token", now, now.plusSeconds(300),
                Map.of("alg", "none"),
                Map.of(
                        "sub", "stable-subject",
                        "preferred_username", "audit-admin",
                        "realm_access", Map.of("roles", List.of("AUDIT_READ", "MEDICAL_AUDIT_READ"))
                )
        );

        var authentication = new JwtAuthConverter("medical-record-service").convert(jwt);

        assertEquals("audit-admin", authentication.getName());
        assertTrue(authentication.getAuthorities().stream().anyMatch(authority ->
                authority.getAuthority().equals("AUDIT_READ")));
        assertTrue(authentication.getAuthorities().stream().anyMatch(authority ->
                authority.getAuthority().equals("MEDICAL_AUDIT_READ")));
    }
}
