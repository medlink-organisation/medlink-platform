package medlink.project.directoyservice.mapper;

import medlink.project.directoyservice.dto.BlocDto;
import medlink.project.directoyservice.entity.Bloc;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BlocMapper {

    @Mapping(source = "idBloc", target = "id")
    @Mapping(source = "clinique.id", target = "cliniqueId")
    BlocDto toDto(Bloc bloc);

    @Mapping(source = "id", target = "idBloc")
    @Mapping(target = "clinique", ignore = true)
    Bloc toEntity(BlocDto dto);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "idBloc", ignore = true)
    @Mapping(target = "clinique", ignore = true)
    void updateEntityFromDto(
            BlocDto dto,
            @MappingTarget Bloc bloc
    );

    List<BlocDto> toDtoList(List<Bloc> blocs);
}