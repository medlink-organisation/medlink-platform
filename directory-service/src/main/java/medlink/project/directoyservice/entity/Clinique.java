package medlink.project.directoyservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "clinique")
public class Clinique  extends  AuditableEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
     private  Long id;

     private String nom;
    @Column(unique = true)
     private String adresse;
    @Column(unique = true)
     private String telephone;
    @Column(unique = true)
     private String email;
     private String ville;
     @OneToMany( mappedBy = "clinique")
     private List <Bloc> blocs;
     @OneToMany(mappedBy = "clinique")
     private List<ServiceMedicale> services = new ArrayList<>();
}
