package medlink.project.medicalrecordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMedicalDto {

    private Long id;

    private Long dossierMedicalId;

    private String nomFichier;

    private String typeMime;

    private Long tailleOctets;

    private LocalDateTime dateDepot;
}
