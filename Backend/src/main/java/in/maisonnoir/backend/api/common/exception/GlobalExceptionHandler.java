package in.maisonnoir.backend.api.common.exception;

import in.maisonnoir.backend.api.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "in.maisonnoir.backend")
public class GlobalExceptionHandler {

        // ─── 400 — Validation Errors ─────────────────────────────────────

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                Map<String, String> fieldErrors = new LinkedHashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

                log.warn("Validation failed at {}: {}", request.getRequestURI(), fieldErrors);

                ErrorResponse body = new ErrorResponse(
                                "One or more fields failed validation. Please review and try again.",
                                "ERR_VALIDATION",
                                request.getRequestURI(),
                                fieldErrors);

                return ResponseEntity.badRequest().body(body);
        }

        // ─── 400 — Business Rule Violations ──────────────────────────────

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex,
                        HttpServletRequest request) {
                log.warn("Bad request at {}: {}", request.getRequestURI(), ex.getMessage());

                ErrorResponse body = new ErrorResponse(
                                ex.getMessage(),
                                "ERR_BAD_REQUEST",
                                request.getRequestURI());

                return ResponseEntity.badRequest().body(body);
        }

        // ─── 401 — Authentication Failures ───────────────────────────────

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex,
                        HttpServletRequest request) {
                log.warn("Authentication failed at {}: {}", request.getRequestURI(), ex.getMessage());

                ErrorResponse body = new ErrorResponse(
                                "Invalid email or password. Please check your credentials and try again.",
                                "ERR_AUTHENTICATION",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }

        // ─── 404 — Resource Not Found ────────────────────────────────────

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
                        HttpServletRequest request) {
                log.warn("Resource not found at {}: {}", request.getRequestURI(), ex.getMessage());

                ErrorResponse body = new ErrorResponse(
                                ex.getMessage(),
                                "ERR_NOT_FOUND",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }

        // ─── 409 — Duplicate Resource ────────────────────────────────────

        @ExceptionHandler(DuplicateResourceException.class)
        public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex,
                        HttpServletRequest request) {
                log.warn("Duplicate resource at {}: {}", request.getRequestURI(), ex.getMessage());

                ErrorResponse body = new ErrorResponse(
                                ex.getMessage(),
                                "ERR_DUPLICATE",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

        // ─── 409 — Data Integrity Violation ──────────────────────────────

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                        HttpServletRequest request) {
                log.warn("Data integrity violation at {}: {}", request.getRequestURI(), ex.getMessage());

                ErrorResponse body = new ErrorResponse(
                                "This operation conflicts with existing data. Please verify your input and try again.",
                                "ERR_DATA_INTEGRITY",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

        // ─── 422 — Order Not Modifiable ──────────────────────────────────

        @ExceptionHandler(OrderNotModifiableException.class)
        public ResponseEntity<ErrorResponse> handleOrderNotModifiable(OrderNotModifiableException ex,
                        HttpServletRequest request) {
                log.warn("Order modification rejected at {}: {}", request.getRequestURI(), ex.getMessage());

                ErrorResponse body = new ErrorResponse(
                                ex.getMessage(),
                                "ERR_ORDER_NOT_MODIFIABLE",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
        }

        // ─── 403 — Access Denied ─────────────────────────────────────────

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex,
                        HttpServletRequest request) {
                log.warn("Access denied at {}: {}", request.getRequestURI(), ex.getMessage());

                ErrorResponse body = new ErrorResponse(
                                "Access denied: insufficient permissions to access this resource.",
                                "ERR_FORBIDDEN",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
        }

        // ─── 500 — Catch-All Fallbacks ───────────────────────────────────

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
                log.error("Unhandled runtime exception at {}", request.getRequestURI(), ex);

                ErrorResponse body = new ErrorResponse(
                                "Something went wrong on our end. Please try again later or contact support.",
                                "ERR_INTERNAL",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
                log.error("Unexpected error at {}", request.getRequestURI(), ex);

                ErrorResponse body = new ErrorResponse(
                                "An unexpected error occurred. Please try again later or contact support.",
                                "ERR_UNEXPECTED",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
}
