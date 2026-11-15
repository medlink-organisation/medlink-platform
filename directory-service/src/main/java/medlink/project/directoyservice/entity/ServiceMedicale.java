package medlink.project.directoyservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Audited
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "service_medicale")
public class ServiceMedicale extends  AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String code ;
    private String libelle;
    private String description;
    private boolean actif;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinique_id", nullable = false)
    private Clinique clinique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bloc_id")
    private Bloc bloc;
}