package portfolio.ecommencesite;

import java.util.HashSet;
import java.util.Set;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import portfolio.ecommencesite.model.AppRole;
import portfolio.ecommencesite.model.Role;
import portfolio.ecommencesite.model.User;
import portfolio.ecommencesite.repo.UserRepo;

@SpringBootTest
class ECommencesiteApplicationTests {

	@Autowired
	private UserRepo userRepo;
	@Test
	void contextLoads() {
		User user = new User();
		Set<Role> roles = new HashSet<>();
		roles.add(new Role(AppRole.ROLE_ADMIN));
		user.setRoles(roles);
		user.setPassword("null");
		user.setUserName("null");
		user.setEmail("wdw@csc.com");
		userRepo.save(user);
		User user1 = userRepo.findByUserName("null").orElseThrow(() -> new RuntimeException(""));
		System.out.println(user1.getRoles());
	}

}
