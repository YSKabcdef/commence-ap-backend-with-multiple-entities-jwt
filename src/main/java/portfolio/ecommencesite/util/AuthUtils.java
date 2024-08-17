package portfolio.ecommencesite.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import portfolio.ecommencesite.model.User;
import portfolio.ecommencesite.repo.UserRepo;
import portfolio.ecommencesite.security.services.UserDetailsImpl;

@Component
public class AuthUtils {

    @Autowired
    UserRepo userRepo;

    public String loggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getEmail();
    }
    public Long loggedInId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepo.findByUserName(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
    }
}
