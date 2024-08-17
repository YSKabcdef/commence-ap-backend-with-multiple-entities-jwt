package portfolio.ecommencesite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long productId;
    private String productName;
    private String image;
    private String description;
    private int quantity;
    private double price;
    private double specialPrice;
    private double discount;

}
