package portfolio.ecommencesite.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import portfolio.ecommencesite.model.CartItem;

import java.util.List;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Long> {
    @Query(value = "SELECT ci FROM CartItem ci WHERE ci.cart.cartId = ?2 and ci.product.productId = ?1")
    CartItem findCartItemByProductIdAndCartId(Long productId,Long cartId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartId = ?1")
    List<CartItem> findCartItemsByCartId(Long cartId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.product.productId = ?1 AND ci.cart.user.email=?2")
    CartItem findCartItemByProductIdAndEmail(Long productId,String email);


    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
    void deleteCartItemByProductIdAndCartId(Long cartId, Long productId);
}
