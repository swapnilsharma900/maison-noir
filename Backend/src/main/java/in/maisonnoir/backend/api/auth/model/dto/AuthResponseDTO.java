package in.maisonnoir.backend.api.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;
    private String token;
    private String message;
}
