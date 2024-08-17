package portfolio.ecommencesite.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portfolio.ecommencesite.model.Payment;
@Repository
public interface PaymentRepo extends JpaRepository<Payment,Long> {
}
