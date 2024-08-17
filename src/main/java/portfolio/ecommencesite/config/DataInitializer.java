package portfolio.ecommencesite.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import portfolio.ecommencesite.model.AppRole;
import portfolio.ecommencesite.model.Role;
import portfolio.ecommencesite.repo.RoleRepo;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepo roleRepo;


    @Override
    public void run(String... args) throws Exception {
        if(!roleRepo.existsByRoleName(AppRole.ROLE_USER)){
            roleRepo.save(new Role(AppRole.ROLE_USER));
        }
        if(!roleRepo.existsByRoleName(AppRole.ROLE_ADMIN)){
            roleRepo.save(new Role(AppRole.ROLE_ADMIN));
        }
        if(!roleRepo.existsByRoleName(AppRole.ROLE_SELLER)){
            roleRepo.save(new Role(AppRole.ROLE_SELLER));
        }
    }
}
