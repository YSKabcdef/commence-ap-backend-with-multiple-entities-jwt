package portfolio.ecommencesite.repo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import portfolio.ecommencesite.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByUserName(String userName);

    List<User> findByRoles(Set<portfolio.ecommencesite.model.Role> roles);

    boolean existsByUserName(String userName);

    boolean existsByEmail (String email);


}
