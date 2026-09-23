package in.maisonnoir.backend.api.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SafeTextValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SafeText {
    String message() default "Unsafe text detected";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

