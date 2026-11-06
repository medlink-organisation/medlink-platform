package medlink.project.directoyservice.validation;

import medlink.project.directoyservice.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class GenericEntityFinder {

    public <T> T findByIdOrThrow(
            JpaRepository<T, Long> repository,
            Long id,
            String entityName
    ) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                entityName + " introuvable avec id : " + id
                        )
                );
    }
}