package in.maisonnoir.backend.api.account.model.dto.user;

import in.maisonnoir.backend.api.account.model.dto.address.AddressDTO;
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
public class UserResponseDTO {
    private Long userId;
    private String userName;
    private String email;
    private String role; // read-only, set by system
    private AddressDTO address;
}
