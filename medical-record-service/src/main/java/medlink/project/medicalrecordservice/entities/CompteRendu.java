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
import java.util.UUID;

@Entity
@Table(
        name = "comptes_rendus",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_comptes_rendus_rendez_vous_id",
                columnNames = "rendez_vous_id"
        ),
        indexes = {
                @Index(
                        name = "idx_comptes_rendus_dossier_medical_id",
                        columnList = "dossier_medical_id"
                ),
                @Index(
                        name = "idx_comptes_rendus_rendez_vous_id",
                        columnList = "rendez_vous_id"
                ),
                @Index(
                        name = "idx_comptes_rendus_praticien_id",
                        columnList = "praticien_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class CompteRendu extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "rendez_vous_id", nullable = false)
    private UUID rendezVousId;

    @Column(name = "praticien_id", nullable = false)
    private UUID praticienId;

    @CreationTimestamp
    @Column(name = "date_redaction", nullable = false, updatable = false)
    private LocalDateTime dateRedaction;

    @Column(name = "contenu", nullable = false, columnDefinition = "TEXT")
    private String contenu;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dossier_medical_id", nullable = false)
    private DossierMedical dossierMedical;
}
