package portfolio.ecommencesite.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portfolio.ecommencesite.model.Category;
import portfolio.ecommencesite.model.Product;

import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    public Page<Product> findByCategory(Category category, Pageable pageDetails);

    public Page<Product> findAllByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String productName, String description, Pageable pageDetails);

    public Optional<Product> findByProductName(String productName);
}
