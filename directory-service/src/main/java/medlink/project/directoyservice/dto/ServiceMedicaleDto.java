package medlink.project.directoyservice.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceMedicaleDto {

    private Long id;
    private String code;
    private String libelle;
    private String description;
    private boolean actif;

    private Long cliniqueId;
    private Long blocId;
}