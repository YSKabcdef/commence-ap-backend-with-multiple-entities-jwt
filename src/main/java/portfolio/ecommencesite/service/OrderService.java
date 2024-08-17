package portfolio.ecommencesite.service;

import portfolio.ecommencesite.dto.OrderDto;

public interface OrderService {
    OrderDto placeOrder(Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
