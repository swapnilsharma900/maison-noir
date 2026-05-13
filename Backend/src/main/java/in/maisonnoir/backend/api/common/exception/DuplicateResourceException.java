package in.maisonnoir.backend.api.common.exception;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue, String message) {
        super(String.format("A %s with %s '%s' already exists. %s",
                resourceName.toLowerCase(), fieldName, fieldValue, message));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}

