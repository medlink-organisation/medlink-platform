package medlink.project.notificationservice.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter scopesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                scopesConverter.convert(jwt).stream(),
                extractRealmRoles(jwt).stream()
        ).collect(Collectors.toSet());
        String username = jwt.getClaimAsString("preferred_username");
        return new JwtAuthenticationToken(
                jwt,
                authorities,
                username == null || username.isBlank() ? jwt.getSubject() : username
        );
    }

    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Object realmAccessClaim = jwt.getClaim("realm_access");
        if (!(realmAccessClaim instanceof java.util.Map<?, ?> realmAccess)) {
            return Set.of();
        }
        Object rolesClaim = realmAccess.get("roles");
        if (!(rolesClaim instanceof Collection<?> roles)) {
            return Set.of();
        }
        return roles.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .flatMap(role -> Stream.of(
                        new SimpleGrantedAuthority(role),
                        new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role)
                ))
                .collect(Collectors.toSet());
    }
}
