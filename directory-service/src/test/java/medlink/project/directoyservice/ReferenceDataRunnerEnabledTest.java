package medlink.project.directoyservice;

import medlink.project.directoyservice.repositories.CliniqueRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "medilink.seed.enabled=true")
class ReferenceDataRunnerEnabledTest {

    @Autowired CommandLineRunner initializeReferenceData;
    @Autowired CliniqueRepository repository;
    @MockitoBean JwtDecoder jwtDecoder;

    @Test
    void runnerIsCreatedAndSeedsClinicsWhenEnabled() {
        assertThat(initializeReferenceData).isNotNull();
        assertThat(repository.findByNomIgnoreCase("Clinique Internationale de Sousse")).isPresent();
        assertThat(repository.findByNomIgnoreCase("Clinique Médicale de Sfax")).isPresent();
        assertThat(repository.findByNomIgnoreCase("Clinique Les Jardins de Tunis")).isPresent();
    }
}
