package medlink.project.directoyservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Bloc  extends  AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBloc ;
    private String code ;
    private String nom ;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinique_id")
    private Clinique clinique;
}
