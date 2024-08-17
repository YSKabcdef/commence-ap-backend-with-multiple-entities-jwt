package portfolio.ecommencesite.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long cartId;

    private UserInfoDto user;

    private double totalPrice;

    private List<ProductDto> cartItems;
}
