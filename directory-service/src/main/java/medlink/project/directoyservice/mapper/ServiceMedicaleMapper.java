package medlink.project.directoyservice.mapper;

import medlink.project.directoyservice.dto.ServiceMedicaleDto;
import medlink.project.directoyservice.entity.ServiceMedicale;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMedicaleMapper {

    @Mapping(source = "clinique.id", target = "cliniqueId")
    @Mapping(source = "bloc.idBloc", target = "blocId")
    ServiceMedicaleDto toDto(ServiceMedicale service);

    @Mapping(target = "clinique", ignore = true)
    @Mapping(target = "bloc", ignore = true)
    ServiceMedicale toEntity(ServiceMedicaleDto dto);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clinique", ignore = true)
    @Mapping(target = "bloc", ignore = true)
    void updateEntityFromDto(
            ServiceMedicaleDto dto,
            @MappingTarget ServiceMedicale service
    );

    List<ServiceMedicaleDto> toDtoList(
            List<ServiceMedicale> services
    );
}