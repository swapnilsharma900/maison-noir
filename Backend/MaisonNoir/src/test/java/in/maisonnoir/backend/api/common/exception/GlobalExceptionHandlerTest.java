package in.maisonnoir.backend.api.common.exception;

import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new DummyController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\"}")) // Invalid payload
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("ERR_VALIDATION"))
                .andExpect(jsonPath("$.message").value("One or more fields failed validation. Please review and try again."))
                .andExpect(jsonPath("$.fieldErrors.name").value("must not be blank"));
    }

    @Test
    void testBadRequestException() throws Exception {
        mockMvc.perform(get("/test/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("ERR_BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Simulated bad request"));
    }

    @Test
    void testBadCredentialsException() throws Exception {
        mockMvc.perform(get("/test/bad-credentials"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("ERR_AUTHENTICATION"))
                .andExpect(jsonPath("$.message").value("Invalid email or password. Please check your credentials and try again."));
    }

    @Test
    void testResourceNotFoundException() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ERR_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("The requested resource could not be found (searched by id: '1')"));
    }

    @Test
    void testDuplicateResourceException() throws Exception {
        mockMvc.perform(get("/test/duplicate"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("ERR_DUPLICATE"))
                .andExpect(jsonPath("$.message").value("Simulated duplicate resource"));
    }

    @Test
    void testDataIntegrityViolationException() throws Exception {
        mockMvc.perform(get("/test/data-integrity"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("ERR_DATA_INTEGRITY"))
                .andExpect(jsonPath("$.message").value("This operation conflicts with existing data. Please verify your input and try again."));
    }

    @Test
    void testOrderNotModifiableException() throws Exception {
        mockMvc.perform(get("/test/order-not-modifiable"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").value("ERR_ORDER_NOT_MODIFIABLE"))
                .andExpect(jsonPath("$.message").value("Order #1 cannot be modify — current status is 'DELIVERED'"));
    }

    @Test
    void testRuntimeException() throws Exception {
        mockMvc.perform(get("/test/runtime-exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("ERR_INTERNAL"))
                .andExpect(jsonPath("$.message").value("Something went wrong on our end. Please try again later or contact support."));
    }

    @Test
    void testException() throws Exception {
        mockMvc.perform(get("/test/exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("ERR_UNEXPECTED"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred. Please try again later or contact support."));
    }

    @RestController
    static class DummyController {

        static class DummyDto {
            @NotBlank(message = "must not be blank")
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        @PostMapping("/test/validation")
        public void validation(@Valid @RequestBody DummyDto dto) {
        }

        @GetMapping("/test/bad-request")
        public void badRequest() {
            throw new BadRequestException("Simulated bad request");
        }

        @GetMapping("/test/bad-credentials")
        public void badCredentials() {
            throw new BadCredentialsException("Simulated bad credentials");
        }

        @GetMapping("/test/not-found")
        public void notFound() {
            throw new ResourceNotFoundException("Resource", "id", 1L);
        }

        @GetMapping("/test/duplicate")
        public void duplicate() {
            throw new DuplicateResourceException("Simulated duplicate resource");
        }

        @GetMapping("/test/data-integrity")
        public void dataIntegrity() {
            throw new DataIntegrityViolationException("Simulated data integrity violation");
        }

        @GetMapping("/test/order-not-modifiable")
        public void orderNotModifiable() {
            throw new OrderNotModifiableException(1L, OrderStatus.DELIVERED, "modify");
        }

        @GetMapping("/test/runtime-exception")
        public void runtimeException() {
            throw new RuntimeException("Simulated runtime exception");
        }

        @GetMapping("/test/exception")
        public void exception() throws Exception {
            throw new Exception("Simulated generic exception");
        }
    }
}
