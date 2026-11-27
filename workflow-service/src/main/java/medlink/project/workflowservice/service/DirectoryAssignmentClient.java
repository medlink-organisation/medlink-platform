package medlink.project.workflowservice.service;

import medlink.project.workflowservice.exception.WorkflowStateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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
            @Value("${medlink.directory.base-url:http://directory-service:8060}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public void requireActiveAssignment(long praticienId, long cliniqueId) {
        AssignmentPage page;
        try {
            page = restClient.get()
                    .uri(uri -> uri
                            .path("/affectations-praticiens/booking/active/by-praticien/{id}")
                            .queryParam("page", 0)
                            .queryParam("size", 500)
                            .build(praticienId))
                    .header(HttpHeaders.AUTHORIZATION, currentBearerToken())
                    .retrieve()
                    .body(AssignmentPage.class);
        } catch (WorkflowStateException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            throw new WorkflowStateException(
                    "Impossible de vérifier l'affectation du praticien à la clinique.");
        }

        boolean active = page != null && page.content() != null
                && page.content().stream().anyMatch(item -> item.cliniqueId() == cliniqueId
                && Boolean.TRUE.equals(item.actif())
                && !Boolean.TRUE.equals(item.deleted()));
        if (!active) {
            throw new WorkflowStateException(
                    "Le praticien n'est plus affecté à la clinique sélectionnée.");
        }
    }

    private String currentBearerToken() {
        if (SecurityContextHolder.getContext().getAuthentication()
                instanceof JwtAuthenticationToken authentication) {
            return "Bearer " + authentication.getToken().getTokenValue();
        }
        throw new WorkflowStateException("Le contexte d'authentification patient est absent.");
    }

    private record AssignmentPage(List<Assignment> content) {
    }

    private record Assignment(long cliniqueId, Boolean actif, Boolean deleted) {
    }
}
