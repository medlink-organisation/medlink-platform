package medlink.project.medicalrecordservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "documents_medicaux",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_documents_medicaux_object_key",
                columnNames = "object_key"
        ),
        indexes = {
                @Index(
                        name = "idx_documents_medicaux_dossier_medical_id",
                        columnList = "dossier_medical_id"
                ),
                @Index(
                        name = "idx_documents_medicaux_object_key",
                        columnList = "object_key"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class DocumentMedical extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_fichier", nullable = false)
    private String nomFichier;

    @Column(name = "type_mime", nullable = false)
    private String typeMime;

    @Column(name = "object_key", nullable = false, unique = true, length = 512)
    private String objectKey;

    @Column(name = "checksum", nullable = false, length = 64)
    private String checksum;

    @Column(name = "taille_octets", nullable = false)
    private Long tailleOctets;

    @CreationTimestamp
    @Column(name = "date_depot", nullable = false, updatable = false)
    private LocalDateTime dateDepot;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dossier_medical_id", nullable = false)
    private DossierMedical dossierMedical;
}
