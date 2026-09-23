package in.maisonnoir.backend.api.order.model.dto;

import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderStatusDTO {
    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus;
}
