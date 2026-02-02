package in.maisonnoir.backend.api.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;


public class SafeTextValidator implements ConstraintValidator<SafeText, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        // Clean the input by stripping unsafe HTML/JS
        String sanitized = Jsoup.clean(value, Safelist.none());

        // Replace the original value with sanitized text
        if (!sanitized.equals(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Input contained unsafe content and was sanitized")
                    .addConstraintViolation();
        }

        return sanitized.equals(value); // reject if unsafe content was present
    }
}

