package in.maisonnoir.backend.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.maisonnoir.backend.api.auth.model.dto.LoginDTO;
import in.maisonnoir.backend.api.auth.model.dto.RegisterDTO;
import in.maisonnoir.backend.api.user.repository.UserDAO;
import in.maisonnoir.backend.api.cart.repository.CartDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CartDAO cartDAO;

    @AfterEach
    void tearDown() {
        // Cleanup created data
        cartDAO.deleteAll();
        userDAO.deleteAll();
    }

    @Test
    void testAuthWorkflowWithJwt() throws Exception {
        // 1. Register a new user
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setFirstName("John");
        registerDTO.setLastName("Doe");
        registerDTO.setEmail("john.doe@example.com");
        registerDTO.setPassword("Password123!");
        registerDTO.setConfirmPassword("Password123!");
        registerDTO.setPhone("1234567890");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.token").exists());

        // 2. Login to get token
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("john.doe@example.com");
        loginDTO.setPassword("Password123!");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").exists())
                .andReturn();

        String response = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(response).get("data").get("token").asText();

        // 3. Access protected endpoint WITHOUT token (Expect 401 Unauthorized via JwtAuthenticationEntryPoint)
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isUnauthorized());

        // 4. Access protected endpoint WITH valid token (Expect 200 OK)
        mockMvc.perform(get("/api/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cart fetched successfully"));
    }
}
