package portfolio.ecommencesite.service;

import portfolio.ecommencesite.dto.CartDto;

import java.util.List;

public interface CartService {
    CartDto addProductToCart(Long productId, int quantity);

    List<CartDto> getAllCarts();

    CartDto getCart(Long cartId);

    CartDto updateProductQuantityInCart(Long productId, int delete);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCart(Long productId, Long cartId);
}
