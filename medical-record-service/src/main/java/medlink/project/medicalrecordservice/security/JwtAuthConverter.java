package medlink.project.medicalrecordservice.security;

import medlink.project.medicalrecordservice.config.properties.JwtProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();
    private final String resourceId;

    public JwtAuthConverter(JwtProperties properties) {
        this.resourceId = properties.resourceId();
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                Stream.concat(
                        extractRoles(jwt.getClaim("realm_access")).stream(),
                        extractClientRoles(jwt).stream()
                )
        ).collect(Collectors.toSet());
        String username = jwt.getClaimAsString("preferred_username");

        if (username == null) {
            username = jwt.getSubject();
        }

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Le JWT ne contient aucune identité exploitable.");
        }
        return new JwtAuthenticationToken(
                jwt,
                authorities,
                username
        );
    }

    private Collection<GrantedAuthority> extractClientRoles(Jwt jwt) {
        Object resourceAccessClaim = jwt.getClaim("resource_access");
        if (!(resourceAccessClaim instanceof Map<?, ?> resourceAccess)) {
            return Set.of();
        }
        return extractRoles(resourceAccess.get(resourceId));
    }

    private Collection<GrantedAuthority> extractRoles(Object accessClaim) {
        if (!(accessClaim instanceof Map<?, ?> access)) {
            return Set.of();
        }
        Object rolesClaim = access.get("roles");
        if (!(rolesClaim instanceof Collection<?> roles)) {
            return Set.of();
        }

        return roles.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toUnmodifiableSet());
    }
}
