package medlink.project.directoyservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class DirectoyServiceApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void contextLoads() {
        org.assertj.core.api.Assertions.assertThat(
                applicationContext.containsBean("initializeReferenceData")
        ).isFalse();
    }

}
