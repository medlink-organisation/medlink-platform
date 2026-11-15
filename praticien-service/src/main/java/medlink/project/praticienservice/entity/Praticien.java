package medlink.project.praticienservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Audited;
import tools.jackson.databind.annotation.EnumNaming;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Table(name = "praticien")
public class Praticien extends  AuditableEntity {
    @Id  @GeneratedValue
    private   Long id ;
    @NotBlank
    @Column(name = "keycloak_id", nullable = false, unique = true)
    private String keycloakId;
    private String  nom ;
    private String  prenom;
    @Column (unique = true ,  nullable = false)
    private String email ;
    private String phone ;
    private Boolean  actif ;
    @Column(unique = true , length = 50)
    private  String numeroOrdre ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialite_id")
    private Specialite specialite;
}
