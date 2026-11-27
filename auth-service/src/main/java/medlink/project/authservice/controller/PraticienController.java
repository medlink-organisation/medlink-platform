package medlink.project.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import medlink.project.authservice.dto.CreatePraticienRequest;
import medlink.project.authservice.dto.PraticienResponse;
import medlink.project.authservice.service.PraticienRegistrationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/practitioners")
@RequiredArgsConstructor
public class PraticienController {

    private final PraticienRegistrationService registrationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PraticienResponse> create(
            @Valid @RequestBody CreatePraticienRequest request,
            JwtAuthenticationToken authentication
    ) {

        String adminToken = authentication
                .getToken()
                .getTokenValue();

        PraticienResponse response =
                registrationService.createPraticien(
                        request,
                        adminToken
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{keycloakId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteKeycloakAccount(
            @PathVariable String keycloakId
    ) {
        registrationService.deleteKeycloakAccount(keycloakId);
        return ResponseEntity.noContent().build();
    }
}
