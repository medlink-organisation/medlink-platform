package medlink.project.praticienservice.mapper;

import medlink.project.praticienservice.dto.PraticienDto;
import medlink.project.praticienservice.entity.Praticien;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PraticienMapper {

    @Mapping(target = "specialiteId", source = "specialite.id")
    @Mapping(target = "specialiteNom", source = "specialite.nom")
    PraticienDto toDto(Praticien entity);

    @Mapping(target = "specialite", ignore = true)
    Praticien toEntity(PraticienDto dto);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "specialite", ignore = true)
    void updateEntity(
            PraticienDto dto,
            @MappingTarget Praticien entity
    );
}
