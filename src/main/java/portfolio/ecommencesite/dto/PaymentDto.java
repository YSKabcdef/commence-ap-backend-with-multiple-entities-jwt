package portfolio.ecommencesite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private Long paymentId;




    private String paymentMethod;

    private String pgPaymentId;
    private String pgStatus;

    private String pgResponseMessage;
    private String pgName;
}
