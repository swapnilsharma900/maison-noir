package in.maisonnoir.backend.api.account.model.dto.address;

import in.maisonnoir.backend.api.common.validation.SafeText;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    @NotBlank(message = "Address Line 1 should not be empty")
    @SafeText
    private String lineOne; // Flat No, House No

    @NotBlank(message = "Address Line 2 is required")
    @SafeText
    private String lineTwo; // Building, society, colony, street

    @SafeText
    private String landmark;

    @NotBlank(message = "City is required")
    @SafeText
    private String city;

    @NotBlank(message = "State is required")
    @SafeText
    private String state;

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp="\\d{6}", message = "Pincode must be 6 digits")
    @SafeText
    private String pincode;

    @NotBlank(message = "Country is required")
    @SafeText
    private String country;
}
