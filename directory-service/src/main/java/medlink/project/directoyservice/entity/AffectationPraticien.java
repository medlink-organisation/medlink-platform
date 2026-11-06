package medlink.project.directoyservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "affectation_praticien")
public class AffectationPraticien extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "praticien_id", nullable = false)
    private Long praticienId;

    @Column(nullable = false)
    private boolean actif = true;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "date_affectation")
    private LocalDateTime dateAffectation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "clinique_id",
            nullable = false
    )
    private Clinique clinique;


    public void desaffecter() {
        this.actif = false;
    }

    public void reactiver() {
        this.actif = true;
        this.deleted = false;
    }

    public void softDelete() {
        this.actif = false;
        this.deleted = true;
    }
}