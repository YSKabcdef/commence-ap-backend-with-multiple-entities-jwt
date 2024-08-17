package portfolio.ecommencesite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolio.ecommencesite.dto.OrderDto;
import portfolio.ecommencesite.dto.OrderRequestDto;
import portfolio.ecommencesite.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDto> orderProducts(@PathVariable String paymentMethod,
                                                  @RequestBody OrderRequestDto orderRequestDto){
        OrderDto orderDto = orderService.placeOrder(
                orderRequestDto.getAddressId(),
                paymentMethod,
                orderRequestDto.getPgName(),
                orderRequestDto.getPgPaymentId(),
                orderRequestDto.getPgStatus(),
                orderRequestDto.getPgResponseMessage())
        ;
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }
}
