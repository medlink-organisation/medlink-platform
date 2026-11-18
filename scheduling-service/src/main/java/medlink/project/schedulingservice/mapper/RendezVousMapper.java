package medlink.project.schedulingservice.mapper;

import medlink.project.schedulingservice.dto.RendezVousDto;
import medlink.project.schedulingservice.entities.RendezVous;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RendezVousMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creneau", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "version", ignore = true)
    RendezVous toEntity(RendezVousDto dto);
    @Mapping(
            source = "creneau.id",
            target = "creneauId"
    )
    RendezVousDto toDto(RendezVous rendezVous);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creneau", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)

    void update (RendezVousDto dto, @MappingTarget RendezVous rendezVous);







}
