package medlink.project.schedulingservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Entity
@Audited
@Table(
        name = "rendez_vous",
        indexes = {
                @Index(
                        name = "idx_rdv_creneau",
                        columnList = "creneau_id"
                ),
                @Index(
                        name = "idx_rdv_patient",
                        columnList = "patient_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVous extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "creneau_id",
            nullable = false
    )
    private Creneau creneau;

    @Column(
            name = "patient_id",
            nullable = false
    )
    private Long patientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusRendezVous status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeRendezVous type;

    @Version
    private Long version;
}
