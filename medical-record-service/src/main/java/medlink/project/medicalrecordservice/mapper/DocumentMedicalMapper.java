package medlink.project.medicalrecordservice.mapper;

import medlink.project.medicalrecordservice.dto.DocumentMedicalDto;
import medlink.project.medicalrecordservice.entities.DocumentMedical;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DocumentMedicalMapper {

    @Mapping(source = "dossierMedical.id", target = "dossierMedicalId")
    DocumentMedicalDto toDto(DocumentMedical entity);
}
