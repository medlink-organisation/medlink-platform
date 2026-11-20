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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "resultats_examens",
        indexes = @Index(
                name = "idx_resultats_examens_dossier_medical_id",
                columnList = "dossier_medical_id"
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class ResultatExamen extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "type_examen", nullable = false, length = 150)
    private String typeExamen;

    @Column(name = "date_examen", nullable = false)
    private LocalDate dateExamen;

    @Column(name = "conclusion", columnDefinition = "TEXT")
    private String conclusion;

    @CreationTimestamp
    @Column(name = "date_publication", nullable = false, updatable = false)
    private LocalDateTime datePublication;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dossier_medical_id", nullable = false)
    private DossierMedical dossierMedical;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_medical_id", nullable = false, unique = true)
    private DocumentMedical document;
}
