package medlink.project.directoyservice.mapper;

import medlink.project.directoyservice.dto.CliniqueDto;
import medlink.project.directoyservice.entity.Clinique;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                BlocMapper.class,
                ServiceMedicaleMapper.class
        }
)
public interface CliniqueMapper {

    CliniqueDto toDto(Clinique clinique);

    @Mapping(target = "blocs", ignore = true)
    @Mapping(target = "services", ignore = true)
    Clinique toEntity(CliniqueDto dto);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "blocs", ignore = true)
    @Mapping(target = "services", ignore = true)
    void updateEntityFromDto(
            CliniqueDto dto,
            @MappingTarget Clinique clinique
    );

    List<CliniqueDto> toDtoList(
            List<Clinique> cliniques
    );
}