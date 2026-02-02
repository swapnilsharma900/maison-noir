package in.maisonnoir.backend.api.common.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue, String message) {
        super(String.format("%s already exists with %s : '%s' \n%s", resourceName, fieldName, fieldValue, message));
    }

}
