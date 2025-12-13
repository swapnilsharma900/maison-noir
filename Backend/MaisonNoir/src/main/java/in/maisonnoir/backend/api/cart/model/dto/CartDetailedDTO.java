package in.maisonnoir.backend.api.cart.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetailedDTO {

    private Long userId;
    private List<CartItemDetailedDTO> items;

    private double totalAmount;
    private int totalItems;
}

