package in.maisonnoir.backend.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        boolean success,
        String message,
        String errorCode,
        Instant timestamp,
        String path,
        Map<String, String> fieldErrors
) {
    /** Standard error — no field-level details. */
    public ErrorResponse(String message, String errorCode, String path) {
        this(false, message, errorCode, Instant.now(), path, null);
    }

    /** Validation error — includes a map of field → message. */
    public ErrorResponse(String message, String errorCode, String path, Map<String, String> fieldErrors) {
        this(false, message, errorCode, Instant.now(), path, fieldErrors);
    }
}
