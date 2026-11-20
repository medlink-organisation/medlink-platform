package medlink.project.medicalrecordservice.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "dossiers_medicaux",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_dossiers_medicaux_patient_id",
                columnNames = "patient_id"
        ),
        indexes = @Index(
                name = "idx_dossiers_medicaux_patient_id",
                columnList = "patient_id"
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class DossierMedical extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    @Column(name = "derniere_modification", nullable = false)
    private LocalDateTime derniereModification;

    @Builder.Default
    @OneToMany(
            mappedBy = "dossierMedical",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ResultatExamen> resultatsExamens = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "dossierMedical",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CompteRendu> comptesRendus = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "dossierMedical",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DocumentMedical> documents = new ArrayList<>();
}
