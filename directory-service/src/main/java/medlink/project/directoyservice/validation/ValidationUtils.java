package medlink.project.directoyservice.validation;

import medlink.project.directoyservice.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.function.BooleanSupplier;

@Component
public class ValidationUtils {

    public void throwIfExists(
            BooleanSupplier condition,
            String message
    ) {

        if (condition.getAsBoolean()) {
            throw new BusinessException(message);
        }
    }

    public void throwIf(
            boolean condition,
            String message
    ) {

        if (condition) {
            throw new BusinessException(message);
        }
    }
}