package in.maisonnoir.backend.api.common.exception;

import in.maisonnoir.backend.api.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        // Log full details internally
        log.warn("Validation failed at {}: {}", request.getRequestURI(), errors);

        // Return generic message with a single code; optionally include fieldErrors in a separate field
        ErrorResponse responseBody = new ErrorResponse(
                "Validation failed",
                "ERR_VALIDATION",
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex,  HttpServletRequest request) {
        log.error("Runtime exception at {}", request.getRequestURI(), ex);

        ErrorResponse responseBody = new ErrorResponse(
                "A runtime error occurred",
                "ERR_RUNTIME",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex,  HttpServletRequest request) {
        // Log full stack trace internally, return generic message externally
        log.error("Unexpected error at {}", request.getRequestURI(), ex);
        ErrorResponse responseBody = new ErrorResponse(
                "An unexpected error occurred",
                "ERR_UNEXPECTED",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("Resource not found at {}: {}", request.getRequestURI(), ex.getMessage());
        ErrorResponse responseBody = new ErrorResponse(
                "Resource Not Found",
                "ERR_NOT_FOUND",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex,  HttpServletRequest request) {
        log.warn("Data integrity violation at {}: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponse responseBody = new ErrorResponse(
                "Data integrity violation",
                "ERR_DATA_INTEGRITY",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex, HttpServletRequest request) {
        log.info("Duplicate resource at {}: {}", request.getRequestURI(), ex.getMessage());
        ErrorResponse responseBody = new ErrorResponse(
                "Duplicate resource",
                "ERR_DUPLICATE",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }
}
