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
    private String mainLine; // Flat No, House No

    @NotBlank(message = "locality is required")
    @SafeText
    private String locality; // Building, society, colony, street

    @SafeText
    private String landmark;

    @NotBlank(message = "city is required")
    @SafeText
    private String city;

    @NotBlank(message = "state is required")
    @SafeText
    private String state;


    @NotBlank(message = "postal code is required")
    @Pattern(regexp="\\d{6}", message = "Postal code must be 6 digit")
    @SafeText
    private String postalCode;

    @NotBlank(message = "country is required")
    @SafeText
    private String country;

}
