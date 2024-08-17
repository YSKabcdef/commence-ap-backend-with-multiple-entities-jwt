package portfolio.ecommencesite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import portfolio.ecommencesite.dto.UserInfoDto;
import portfolio.ecommencesite.exception.ApiException;
import portfolio.ecommencesite.model.AppRole;
import portfolio.ecommencesite.model.Role;
import portfolio.ecommencesite.model.User;
import portfolio.ecommencesite.repo.RoleRepo;
import portfolio.ecommencesite.repo.UserRepo;
import portfolio.ecommencesite.security.jwt.JwtUtils;
import portfolio.ecommencesite.security.jwt.LoginRequest;
import portfolio.ecommencesite.security.jwt.LoginResponse;
import portfolio.ecommencesite.security.jwt.SignUpRequest;
import portfolio.ecommencesite.security.services.UserDetailsImpl;
import portfolio.ecommencesite.service.AuthService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public LoginResponse signInUser(LoginRequest loginRequest) {
        Authentication authentication;
        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername()
                ,loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtToken = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).toList();
        return new LoginResponse(jwtToken, userDetails.getUsername(),roles, userDetails.getId());
    }

    @Override
    public UserInfoDto signUpUser(SignUpRequest signUpRequest) {
        if (userRepo.existsByUserName(signUpRequest.getUsername())){
            throw new ApiException("Error: username already taken!");
        }
        if (userRepo.existsByEmail(signUpRequest.getEmail())){
            throw new ApiException("Error: email already taken!");
        }
        User user = new User();
        user.setUserName(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        if(strRoles==null){
            Role userRole = roleRepo.findByRoleName(AppRole.ROLE_USER).orElseThrow(()->new ApiException("Error : Role not found"));
            roles.add(userRole);
        }
        else{
            strRoles.forEach(
                    role-> {
                        switch (role){
                            case "admin":
                                Role adminRole = roleRepo.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(()->new ApiException("Error : Role not found"));
                                roles.add(adminRole);
                                break;

                            case "seller":
                                Role modRole = roleRepo.findByRoleName(AppRole.ROLE_SELLER).orElseThrow(()->new ApiException("Error : Role not found"));
                                roles.add(modRole);
                                break;
                            default:
                                Role userRole = roleRepo.findByRoleName(AppRole.ROLE_USER).orElseThrow(()->new ApiException("Error : Role not found"));
                                roles.add(userRole);
                                break;
                        }
                    });
            user.setRoles(roles);

        }
        return modelMapper.map(userRepo.save(user),UserInfoDto.class);


    }

    @Override
    public String getUsername(Authentication authentication) {

        return authentication == null ? "NULL" : authentication.getName();
    }

    @Override
    public UserInfoDto getUser(Authentication authentication) {
        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            ResponseCookie jwtToken = jwtUtils.generateJwtCookie(userDetails);
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).toList();
            User user = userRepo.findByUserName(userDetails.getUsername())
                    .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
            UserInfoDto userInfoDto = modelMapper.map(user,UserInfoDto.class);
            return userInfoDto;
        }
        return null;
    }

    @Override
    public ResponseCookie signoutUser() {
        return jwtUtils.getCleanJwtCookie();
    }
}
