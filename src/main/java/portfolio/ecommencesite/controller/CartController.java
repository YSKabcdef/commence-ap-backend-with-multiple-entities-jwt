package portfolio.ecommencesite.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolio.ecommencesite.dto.CartDto;
import portfolio.ecommencesite.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDto> addProductToCart(@PathVariable("productId") Long productId,
                                                    @PathVariable("quantity") int quantity){
        return new ResponseEntity<>(cartService.addProductToCart(productId, quantity), HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDto>> getCarts(){
        List<CartDto> cartDtos = cartService.getAllCarts();

        return ResponseEntity.ok(cartDtos);

    }
    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDto> getCartById(@RequestParam(name = "cartId") Long cartId){
        CartDto cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDto);
    }

    @GetMapping("/carts/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDto> updateCartProduct(@PathVariable("productId") Long productId, @PathVariable("operation") String operation){
        CartDto cartDto = cartService.updateProductQuantityInCart(productId,operation.equalsIgnoreCase("delete") ? -1 :1 );

        return ResponseEntity.ok(cartDto);
    }
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId){
        String status = cartService.deleteProductFromCart(cartId,productId);

        return ResponseEntity.ok(status);
    }
}
