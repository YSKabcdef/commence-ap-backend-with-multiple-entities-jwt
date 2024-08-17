package portfolio.ecommencesite.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portfolio.ecommencesite.model.AppRole;
import portfolio.ecommencesite.model.Role;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {
    public Optional<Role> findByRoleName(AppRole roleName);

    boolean existsByRoleName(AppRole roleName);

}
