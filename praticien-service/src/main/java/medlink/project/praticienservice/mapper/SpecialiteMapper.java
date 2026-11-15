
package medlink.project.praticienservice.mapper;

import medlink.project.praticienservice.dto.SpecialiteDto;
import medlink.project.praticienservice.entity.Specialite;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SpecialiteMapper {

    SpecialiteDto toDto(Specialite entity);

    Specialite toEntity(SpecialiteDto dto);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    void updateEntity(
            SpecialiteDto dto,
            @MappingTarget Specialite entity
    );
}