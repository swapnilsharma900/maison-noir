package in.maisonnoir.backend.api.common.response;

import java.time.Instant;

public record ErrorResponse(
        boolean success,
        String message,
        String errorCode, // e.g., ERR_VALIDATION, ERR_NOT_FOUND, ERR_UNEXPECTED
        Instant timestamp,
        String path // request URI
) {
    public ErrorResponse(String message, String errorCode, String path) {
        this(false, message, errorCode, Instant.now(), path);
    }
}
