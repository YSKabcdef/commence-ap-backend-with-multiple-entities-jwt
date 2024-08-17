package portfolio.ecommencesite.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portfolio.ecommencesite.model.Category;

import java.util.Optional;
@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {
    public Optional<Category> findByCategoryName(String categoryName);
}
