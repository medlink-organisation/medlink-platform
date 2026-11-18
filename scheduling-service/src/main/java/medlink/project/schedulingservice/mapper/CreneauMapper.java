package medlink.project.schedulingservice.mapper;

import medlink.project.schedulingservice.dto.CreneauDto;
import medlink.project.schedulingservice.entities.Creneau;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface CreneauMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "version", ignore = true)
    Creneau toEntity (CreneauDto dto ) ;


    CreneauDto toDto(Creneau creneau) ;


@BeanMapping( nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@Mapping(target = "id", ignore = true)
@Mapping(target = "statut", ignore = true)
@Mapping(target = "version", ignore = true)
@Mapping(target = "createdAt", ignore = true)
@Mapping(target = "updatedAt", ignore = true)
@Mapping(target = "createdBy", ignore = true)
@Mapping(target = "updatedBy", ignore = true)

    void update(CreneauDto dto , @MappingTarget Creneau  creneau);

}
