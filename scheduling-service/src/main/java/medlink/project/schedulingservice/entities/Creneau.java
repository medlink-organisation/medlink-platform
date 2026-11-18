package medlink.project.schedulingservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "creneaux",
        indexes = {
                @Index(
                        name = "idx_creneau_praticien",
                        columnList = "praticien_id"
                ),
                @Index(
                        name = "idx_creneau_clinique",
                        columnList = "clinique_id"
                ),
                @Index(
                        name = "idx_creneau_dates",
                        columnList = "date_debut,date_fin"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Creneau extends AuditableEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(
             name = "praticien_id",
             nullable = false
     )
     private Long praticienId;

     @Column(
             name = "clinique_id",
             nullable = false
     )
     private Long cliniqueId;

     @Column(
             name = "date_debut",
             nullable = false
     )
     private LocalDateTime dateDebut;

     @Column(
             name = "date_fin",
             nullable = false
     )
     private LocalDateTime dateFin;

     @Enumerated(EnumType.STRING)
     @Column(nullable = false)
     private StatutCreneau statut;

     @Version
     private Long version;
}