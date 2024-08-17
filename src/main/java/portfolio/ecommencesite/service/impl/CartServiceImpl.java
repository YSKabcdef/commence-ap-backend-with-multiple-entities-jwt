package portfolio.ecommencesite.service.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portfolio.ecommencesite.dto.CartDto;
import portfolio.ecommencesite.dto.ProductDto;
import portfolio.ecommencesite.exception.ApiException;
import portfolio.ecommencesite.exception.ResourceNotFoundException;
import portfolio.ecommencesite.model.Cart;
import portfolio.ecommencesite.model.CartItem;
import portfolio.ecommencesite.model.Product;
import portfolio.ecommencesite.repo.CartItemRepo;
import portfolio.ecommencesite.repo.CartRepo;
import portfolio.ecommencesite.repo.ProductRepo;
import portfolio.ecommencesite.service.CartService;
import portfolio.ecommencesite.util.AuthUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductRepo productRepo;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    CartItemRepo cartItemRepo;

    @Autowired
    AuthUtils authUtils;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDto addProductToCart(Long productId, int quantity) {
        Cart cart = createCart();

        Product product = productRepo.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","product",productId));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(productId,cart.getCartId());
        if (cartItem!=null) {
            throw new ApiException("Product" + product.getProductName() + " already existed in the card." );
        }
        if (product.getQuantity() == 0 ){
            throw new ApiException(product.getProductName()+" is not available");
        }
        if (product.getQuantity() < quantity){
            throw new ApiException("Please, make an order of the "+ product.getProductName() +
                    " less then or equal to the quantity " + product.getQuantity() + ".");
        }
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        cartItemRepo.save(newCartItem);
        List<CartItem> cartItems = cartItemRepo.findCartItemsByCartId(cart.getCartId());
        cart.setTotalPrice(cart.getTotalPrice()+quantity*product.getSpecialPrice());
        cartRepo.save(cart);
        CartDto cartDto = modelMapper.map(cart,CartDto.class);
        cartDto.setCartItems(cartItems.stream().map(
                item -> {
                    ProductDto productDto = modelMapper.map(
                            item.getProduct(),ProductDto.class
                    );
                    productDto.setQuantity(item.getQuantity());
                    return productDto;
                }
                ).toList());
        return cartDto;
    }

    @Override
    public List<CartDto> getAllCarts() {
        List<Cart> carts = cartRepo.findAll();
        if(carts.isEmpty()){
            throw new ApiException("No cart existed yet");
        }
        List<CartDto> cartDtos = carts.stream().map(
               cart -> {
                   CartDto cartDto = modelMapper.map(cart, CartDto.class);
                   List<ProductDto> productDtos = cart.getCartItems().stream().map(
                           cartItem -> {
                               ProductDto productDto = modelMapper.map(cartItem.getProduct(), ProductDto.class);
                               productDto.setQuantity(cartItem.getQuantity());
                               return productDto;
                           }
                   ).toList();
                   cartDto.setCartItems(productDtos);
                   return cartDto;
               }
        ).toList();

        return cartDtos;
    }

    @Override
    public CartDto getCart(Long cartId) {
        Cart cart = cartRepo.findCartByEmailAndCartId(authUtils.loggedInEmail(),cartId)
                .orElseGet(()->{
                    Cart newCart = new Cart();
                    newCart.setCartItems(new ArrayList<>());
                    newCart.setTotalPrice((double) 0);
                    newCart.setUser(authUtils.loggedInUser());
                    return cartRepo.save(newCart);
                });
        CartDto cartDto = modelMapper.map(cart,CartDto.class);
        cartDto.setCartItems(
                cart.getCartItems().stream().map(
                        item ->{
                            ProductDto productDto = modelMapper.map(item.getProduct(),ProductDto.class);
                            productDto.setQuantity(item.getQuantity());
                            return productDto;
                        }).toList()
        );
        return cartDto;
    }

    @Transactional
    @Override
    public CartDto updateProductQuantityInCart(Long productId, int quantity) {
        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndEmail(productId,authUtils.loggedInEmail());
        Product product = productRepo.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product",
                "product",productId));
        if(cartItem==null){
            throw new ResourceNotFoundException("CartItem","productId",productId);
        }
        if(product.getQuantity()==0){
            throw new ApiException("no more products left");
        }
        if(product.getQuantity()<quantity+cartItem.getQuantity()){
            throw new ApiException("Please create order with quantity less than or equal to " +
                    product.getQuantity());
        }

        if(cartItem.getQuantity() + quantity == 0){

            Cart cart = cartRepo.findCartByEmail(authUtils.loggedInEmail());
            cartItemRepo.deleteCartItemByProductIdAndCartId(cart.getCartId(),productId);

            cart.setTotalPrice(cart.getTotalPrice()+quantity*cartItem.getProductPrice());
            cartRepo.save(cart);
            CartDto cartDto = modelMapper.map(cart,CartDto.class);
            cartDto.setCartItems(
                    cart.getCartItems().stream().map(
                            item ->{
                                ProductDto productDto = modelMapper.map(item.getProduct(),ProductDto.class);
                                productDto.setQuantity(item.getQuantity());
                                return productDto;
                            }
                    ).toList()
            );



         return cartDto;
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepo.save(cartItem);
        }
        Cart cart = cartRepo.findCartByEmail(authUtils.loggedInEmail());
        cart.setTotalPrice(cart.getTotalPrice()+quantity*cartItem.getProductPrice());
        cartRepo.save(cart);
        CartDto cartDto = modelMapper.map(cart,CartDto.class);
        cartDto.setCartItems(
                cart.getCartItems().stream().map(
                        item ->{
                            ProductDto productDto = modelMapper.map(item.getProduct(),ProductDto.class);
                            productDto.setQuantity(item.getQuantity());
                            return productDto;
                        }
                ).toList()
        );
        return cartDto;
    }
    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
       Cart cart = cartRepo.findById(cartId).orElseThrow(()->
               new ResourceNotFoundException("Cart","card",cartId));
        Product product = productRepo.findById(productId).orElseThrow(()->
          new ResourceNotFoundException("Product","product",productId));
       CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(productId,cartId);
       if(cartItem==null){
           throw new ResourceNotFoundException("Cart Item", "productId", productId);
       }
       /*
       List<CartItem> cartItems = cart.getCartItems();
       cartItems.remove(cartItem);
       cart.setCartItems(cartItems);
       cart.setTotalPrice(cart.getTotalPrice()- cartItem.getQuantity()*cartItem.getProductPrice());
       List<CartItem> productCartItem = product.getCartItems();
       productCartItem.remove(cartItem);
       product.setCartItems(productCartItem);
       cartRepo.save(cart);
       productRepo.save(product); */
        cart.setTotalPrice(cart.getTotalPrice()- cartItem.getQuantity()*cartItem.getProductPrice());
        cartRepo.save(cart);
        cartItemRepo.deleteCartItemByProductIdAndCartId(cartId,productId);       return cartItem.getCartItemId() + " is deleted successfully.";
    }

    @Override
    public void updateProductInCart(Long productId, Long cartId) {
        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(productId,cartId);
        if(cartItem == null){
            throw new ResourceNotFoundException("CartItem","CartItem",productId);
        }
        Product product = productRepo.findById(productId).orElseThrow(()->new ResourceNotFoundException("productId",
                "productId",productId));
        Cart cart = cartRepo.findById(cartId).orElseThrow(()->new ResourceNotFoundException("cartId",
                "cartId",cartId));
        cartItem.setProduct(product);
        cartItem.setDiscount(product.getDiscount());
        cartItem.setProductPrice(product.getSpecialPrice());
        if(cartItem.getQuantity()>product.getQuantity()){
            cartItem.setQuantity(product.getQuantity());
        }
        cart.setTotalPrice(cartItem.getQuantity()*product.getSpecialPrice());
        cartItemRepo.save(cartItem);
        cartRepo.save(cart);



    }

    private Cart createCart() {
        Cart userCart = cartRepo.findCartByEmail(authUtils.loggedInEmail());
        if(userCart != null){
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice((double) 0);
        cart.setUser(authUtils.loggedInUser());

        return cartRepo.save(cart);
    }
}
