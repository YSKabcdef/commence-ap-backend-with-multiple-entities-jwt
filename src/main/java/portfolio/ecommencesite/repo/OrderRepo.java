package portfolio.ecommencesite.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portfolio.ecommencesite.model.Order;
@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {
}
