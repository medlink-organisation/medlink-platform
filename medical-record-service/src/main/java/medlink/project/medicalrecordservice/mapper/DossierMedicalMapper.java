package medlink.project.medicalrecordservice.mapper;

import medlink.project.medicalrecordservice.dto.DossierMedicalDto;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DossierMedicalMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "derniereModification", ignore = true)
    @Mapping(target = "resultatsExamens", ignore = true)
    @Mapping(target = "comptesRendus", ignore = true)
    @Mapping(target = "documents", ignore = true)
    DossierMedical toEntity(DossierMedicalDto dto);

    DossierMedicalDto toDto(DossierMedical entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "derniereModification", ignore = true)
    @Mapping(target = "resultatsExamens", ignore = true)
    @Mapping(target = "comptesRendus", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromDto(DossierMedicalDto dto, @MappingTarget DossierMedical entity);
}
