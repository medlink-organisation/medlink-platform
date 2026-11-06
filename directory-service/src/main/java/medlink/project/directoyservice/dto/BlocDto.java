package medlink.project.directoyservice.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlocDto {

    private Long id;
    private String code;
    private String nom;
    private String description;

    private Long cliniqueId;
}