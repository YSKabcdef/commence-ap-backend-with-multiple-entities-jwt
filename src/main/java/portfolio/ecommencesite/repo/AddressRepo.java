package portfolio.ecommencesite.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import portfolio.ecommencesite.model.Address;

import java.util.List;

public interface AddressRepo extends JpaRepository<Address, Long> {

}
