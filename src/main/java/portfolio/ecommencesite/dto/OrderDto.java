package portfolio.ecommencesite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long orderId;
    private String email;
    private List<OrderItemDto> orderItems;
    private LocalDate localDate;
    private PaymentDto payment;
    private double totalAmount;
    private String orderStatus;
    private Long addressId;
}
