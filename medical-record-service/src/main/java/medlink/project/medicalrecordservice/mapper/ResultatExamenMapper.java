package medlink.project.medicalrecordservice.mapper;

import medlink.project.medicalrecordservice.dto.ResultatExamenDto;
import medlink.project.medicalrecordservice.entities.ResultatExamen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ResultatExamenMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "datePublication", ignore = true)
    @Mapping(target = "dossierMedical", ignore = true)
    @Mapping(target = "document", ignore = true)
    ResultatExamen toEntity(ResultatExamenDto dto);

    @Mapping(source = "dossierMedical.id", target = "dossierMedicalId")
    @Mapping(source = "document.id", target = "documentMedicalId")
    ResultatExamenDto toDto(ResultatExamen entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "datePublication", ignore = true)
    @Mapping(target = "dossierMedical", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromDto(ResultatExamenDto dto, @MappingTarget ResultatExamen entity);
}
