package medlink.project.praticienservice.grpc;


import medlink.grpc.praticien.v1.CreatePraticienGrpcRequest;
import medlink.grpc.praticien.v1.PraticienGrpcResponse;
import medlink.project.praticienservice.dto.PraticienDto;

import org.springframework.stereotype.Component;

@Component
public class PraticienGrpcMapper {

    // gRPC Request -> DTO métier
    public PraticienDto toDto(
            CreatePraticienGrpcRequest request
    ) {

        PraticienDto dto = new PraticienDto();

        dto.setKeycloakId(request.getKeycloakId());
        dto.setNom(request.getNom());
        dto.setPrenom(request.getPrenom());
        dto.setEmail(request.getEmail());
        dto.setPhone(request.getPhone());
        dto.setNumeroOrdre(request.getNumeroOrdre());
        dto.setSpecialiteId(request.getSpecialiteId());

        return dto;
    }

    // DTO métier -> gRPC Response
    public PraticienGrpcResponse toGrpc(
            PraticienDto dto
    ) {

        return PraticienGrpcResponse.newBuilder()
                .setId(dto.getId())
                .setKeycloakId(dto.getKeycloakId())
                .setNom(dto.getNom())
                .setPrenom(dto.getPrenom())
                .setEmail(dto.getEmail())
                .setPhone(dto.getPhone())
                .setNumeroOrdre(dto.getNumeroOrdre())
                .setSpecialiteId(dto.getSpecialiteId())
                .setActif(Boolean.TRUE.equals(dto.getActif()))
                .build();
    }
}