package in.maisonnoir.backend.api.order.model.dto;

import in.maisonnoir.backend.api.common.validation.SafeText;
import jakarta.validation.constraints.NotBlank;
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
public class PlaceOrderDTO {

    @NotBlank(message = "Payment method is required")
    @SafeText
    private String paymentMethod; // COD, CARD, UPI, etc.
}
