package portfolio.ecommencesite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    private Long orderItemId;


    private ProductDto product;

    private int quantity;
    private double discount;
    private double orderedProductPrice;
}
