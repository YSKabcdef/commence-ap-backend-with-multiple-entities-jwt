package portfolio.ecommencesite.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    private Long cartItemId;

    private int quantity;

    private double discount;

    private double productPrice;



}
