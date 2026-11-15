package medlink.project.praticienservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import medlink.project.praticienservice.validtaion.OnCreate;
import medlink.project.praticienservice.validtaion.OnUpdate;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SpecialiteDto {


    private Long id;

    @NotBlank(
            message = "Code obligatoire",
            groups = OnCreate.class
    )
    @Pattern(
            regexp = ".*\\S.*",
            message = "Code ne doit pas être vide",
            groups = OnUpdate.class
    )
    @Size(
            max = 50,
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String code;

    @NotBlank(
            message = "Nom obligatoire",
            groups = OnCreate.class
    )
    @Pattern(
            regexp = ".*\\S.*",
            message = "Nom ne doit pas être vide",
            groups = OnUpdate.class
    )
    @Size(
            max = 150,
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String nom;

    @Size(
            max = 500,
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String description;

    private Boolean actif;
}
