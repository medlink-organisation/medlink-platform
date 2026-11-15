package medlink.project.authservice.dto;


import jakarta.validation.constraints.*;

public record CreatePraticienRequest(

        @NotBlank String nom,

        @NotBlank String prenom,

        @NotBlank @Email String email,

        @NotBlank
        @Pattern(regexp = "^\\+[1-9][0-9]{7,14}$")
        String phone,

        @NotBlank String numeroOrdre,

        @NotNull @Positive Long specialiteId

) {}
