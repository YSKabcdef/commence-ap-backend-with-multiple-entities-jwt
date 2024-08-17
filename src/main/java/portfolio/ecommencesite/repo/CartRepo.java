package portfolio.ecommencesite.repo;

import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import portfolio.ecommencesite.model.Cart;
import portfolio.ecommencesite.model.User;

import java.util.List;
import java.util.Optional;
@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
    Cart findCartByEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.cartId = ?2")
    Optional<Cart> findCartByEmailAndCartId(String email,Long cartId);

    @Query("Select c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id = ?1")
    List<Cart> findCartsByProductId(Long productId);
}
