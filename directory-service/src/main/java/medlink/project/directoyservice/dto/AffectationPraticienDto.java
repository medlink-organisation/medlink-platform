package medlink.project.directoyservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AffectationPraticienDto {

    private Long id;

    private Long praticienId;

    private Long cliniqueId;
    private String cliniqueNom;

    private Boolean actif;
    private Boolean deleted;

    private LocalDateTime dateAffectation;
}