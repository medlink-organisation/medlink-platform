package medlink.project.praticienservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import medlink.project.praticienservice.repository.SpecialiteRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PraticienServiceApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SpecialiteRepository specialiteRepository;

    @Test
    void contextLoads() {
        assertThat(applicationContext.containsBean("initializeSpecialties")).isTrue();
        assertThat(specialiteRepository.findByCodeIgnoreCase("MED-GEN")).isPresent();
        assertThat(specialiteRepository.findByCodeIgnoreCase("CARDIO")).isPresent();
        assertThat(specialiteRepository.findByCodeIgnoreCase("PED")).isPresent();
    }

}
