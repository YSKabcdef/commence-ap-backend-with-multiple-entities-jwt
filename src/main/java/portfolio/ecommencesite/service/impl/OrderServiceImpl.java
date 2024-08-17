package portfolio.ecommencesite.service.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portfolio.ecommencesite.dto.OrderDto;
import portfolio.ecommencesite.exception.ApiException;
import portfolio.ecommencesite.exception.ResourceNotFoundException;
import portfolio.ecommencesite.model.*;
import portfolio.ecommencesite.repo.*;
import portfolio.ecommencesite.service.CartService;
import portfolio.ecommencesite.service.OrderService;
import portfolio.ecommencesite.util.AuthUtils;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    AuthUtils authUtils;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    CartItemRepo cartItemRepo;

    @Autowired
    OrderItemRepo orderItemRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    CartService cartService;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PaymentRepo paymentRepo;

    @Override
    @Transactional
    public OrderDto placeOrder(Long addressId,
                               String paymentMethod,
                               String pgName,
                               String pgPaymentId,
                               String pgStatus,
                               String pgResponseMessage) {

        Cart cart = cartRepo.findCartByEmail(authUtils.loggedInEmail());
        if(cart == null){
            throw new ResourceNotFoundException("Cart","email",authUtils.loggedInEmail());
        }
        Address address = addressRepo.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address","address",addressId));

        Order order = new Order();
        order.setOrderStatus("Order Accepted");
        order.setEmail(authUtils.loggedInEmail());
        order.setTotalAmount(cart.getTotalPrice());
        order.setLocalDate(LocalDate.now());
        order.setAddress(address);

        Payment payment= new Payment(paymentMethod,pgPaymentId,pgStatus,pgResponseMessage,pgName);
        payment.setOrder(order);
        paymentRepo.save(payment);
        order.setPayment(payment);
        Order savedOrder = orderRepo.save(order);

        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty()){
            throw new ApiException("Cart is empty");
        }

        List<OrderItem> orderItems = cartItems.stream().map(
                cartItem ->{
                   OrderItem orderItem = new OrderItem();
                   orderItem.setOrder(savedOrder);
                   orderItem.setOrderedProductPrice(cartItem.getProductPrice());
                   orderItem.setProduct(cartItem.getProduct());
                   orderItem.setQuantity(cartItem.getQuantity());
                   orderItem.setDiscount(cartItem.getDiscount());
                   orderItemRepo.save(orderItem);
                   return orderItem;
                }
        ).toList();
        cartItems.stream().forEach(
                cartItem->{
                    Product product = cartItem.getProduct();
                    product.setQuantity(product.getQuantity()-cartItem.getQuantity());
                    productRepo.save(product);
                    cartService.deleteProductFromCart(cart.getCartId(),product.getProductId());
                }
        );
        savedOrder.setOrderItems(orderItems);
        OrderDto orderDto = modelMapper.map(savedOrder,OrderDto.class);
        orderDto.setAddressId(addressId);
        return orderDto;
    }
}
