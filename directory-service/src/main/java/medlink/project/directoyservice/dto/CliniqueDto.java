package medlink.project.directoyservice.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CliniqueDto {

    private Long id;
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private String ville;

    private List<BlocDto> blocs;
    private List<ServiceMedicaleDto> services;
}