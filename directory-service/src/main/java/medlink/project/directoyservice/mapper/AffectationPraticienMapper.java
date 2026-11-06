package medlink.project.directoyservice.mapper;

import medlink.project.directoyservice.dto.AffectationPraticienDto;
import medlink.project.directoyservice.entity.AffectationPraticien;
import medlink.project.directoyservice.entity.Clinique;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AffectationPraticienMapper {

    // Entity -> DTO
    @Mapping(source = "clinique.id", target = "cliniqueId")
    @Mapping(source = "clinique.nom", target = "cliniqueNom")
    AffectationPraticienDto toDto(
            AffectationPraticien entity
    );


    // DTO -> Entity (CREATE)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "clinique", target = "clinique")
    @Mapping(target = "actif", constant = "true")
    @Mapping(target = "deleted", constant = "false")
    @Mapping(
            target = "dateAffectation",
            expression = "java(java.time.LocalDateTime.now())"
    )
    AffectationPraticien toEntity(
            AffectationPraticienDto dto,
            Clinique clinique
    );
    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "clinique", target = "clinique")
    @Mapping(target = "dateAffectation", ignore = true)
    @Mapping(target = "actif", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntity(
            AffectationPraticienDto dto,
            Clinique clinique,
            @MappingTarget AffectationPraticien entity
    );
}