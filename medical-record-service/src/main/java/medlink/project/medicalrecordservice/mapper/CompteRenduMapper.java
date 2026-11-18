package medlink.project.medicalrecordservice.mapper;

import medlink.project.medicalrecordservice.dto.CompteRenduDto;
import medlink.project.medicalrecordservice.entities.CompteRendu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CompteRenduMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateRedaction", ignore = true)
    @Mapping(target = "dossierMedical", ignore = true)
    CompteRendu toEntity(CompteRenduDto dto);

    @Mapping(source = "dossierMedical.id", target = "dossierMedicalId")
    CompteRenduDto toDto(CompteRendu entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rendezVousId", ignore = true)
    @Mapping(target = "praticienId", ignore = true)
    @Mapping(target = "dateRedaction", ignore = true)
    @Mapping(target = "dossierMedical", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromDto(CompteRenduDto dto, @MappingTarget CompteRendu entity);
}
