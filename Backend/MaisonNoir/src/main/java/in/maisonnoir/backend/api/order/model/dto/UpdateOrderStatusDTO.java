package in.maisonnoir.backend.api.order.model.dto;

import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
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
public class UpdateOrderStatusDTO {
    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus;
}
