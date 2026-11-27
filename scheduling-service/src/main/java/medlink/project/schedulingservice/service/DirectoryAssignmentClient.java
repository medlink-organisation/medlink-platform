package medlink.project.schedulingservice.service;

import medlink.project.schedulingservice.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class DirectoryAssignmentClient {
    private final RestClient restClient;

    public DirectoryAssignmentClient(
            RestClient.Builder builder,
            @Value("${medlink.directory.base-url:http://directory-service:8060}") String baseUrl
    ) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public void requireActiveAssignments(Long praticienId, Iterable<Long> cliniqueIds) {
        String bearer = currentBearerToken();
        AssignmentPage page;
        try {
            page = restClient.get()
                    .uri(uri -> uri.path("/affectations-praticiens/active/by-praticien/{id}")
                            .queryParam("page", 0).queryParam("size", 500).build(praticienId))
                    .header(HttpHeaders.AUTHORIZATION, bearer)
                    .retrieve()
                    .body(AssignmentPage.class);
        } catch (RuntimeException exception) {
            throw new BusinessException("Impossible de vérifier les affectations du praticien.");
        }
        List<Long> assigned = page == null || page.content() == null
                ? List.of()
                : page.content().stream()
                    .filter(item -> Boolean.TRUE.equals(item.actif()) && !Boolean.TRUE.equals(item.deleted()))
                    .map(Assignment::cliniqueId).toList();
        for (Long cliniqueId : cliniqueIds) {
            if (!assigned.contains(cliniqueId)) {
                throw new BusinessException("Le praticien n'est pas affecté à la clinique " + cliniqueId + ".");
            }
        }
    }

    private String currentBearerToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwt) {
            return "Bearer " + jwt.getToken().getTokenValue();
        }
        throw new BusinessException("Le contexte d'authentification est requis pour vérifier les affectations.");
    }

    private record AssignmentPage(List<Assignment> content) {}
    private record Assignment(Long cliniqueId, Boolean actif, Boolean deleted) {}
}
