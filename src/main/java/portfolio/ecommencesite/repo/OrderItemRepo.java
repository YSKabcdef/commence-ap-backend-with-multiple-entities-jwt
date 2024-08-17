package portfolio.ecommencesite.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portfolio.ecommencesite.model.OrderItem;
@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {
}
