package in.maisonnoir.backend.api.account.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private String street;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @Pattern(regexp="\\d{6}")
    private String postalCode;

    @NotBlank
    private String country;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private boolean isDefault;

}
