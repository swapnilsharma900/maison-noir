package in.maisonnoir.backend.api.common.exception;

/**
 * Thrown when a client request is semantically invalid due to business rule violations
 * (e.g. password mismatch, empty cart, or malformed input).
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
