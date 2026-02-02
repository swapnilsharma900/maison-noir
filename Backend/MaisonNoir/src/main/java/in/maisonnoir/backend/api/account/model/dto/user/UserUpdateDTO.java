package in.maisonnoir.backend.api.account.model.dto.user;

import in.maisonnoir.backend.api.account.model.dto.address.AddressDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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
public class UserUpdateDTO {
    @Size(min = 3, max = 50, message = "User name must be between 3 and 50 characters")
    private String userName;

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private AddressDTO address;
}
