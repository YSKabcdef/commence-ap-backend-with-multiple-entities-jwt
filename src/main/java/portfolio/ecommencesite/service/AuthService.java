package portfolio.ecommencesite.service;


import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import portfolio.ecommencesite.dto.UserInfoDto;
import portfolio.ecommencesite.security.jwt.LoginRequest;
import portfolio.ecommencesite.security.jwt.LoginResponse;
import portfolio.ecommencesite.security.jwt.SignUpRequest;


public interface AuthService {
    LoginResponse signInUser(LoginRequest loginRequest);

    UserInfoDto signUpUser(SignUpRequest signUpRequest);

    String getUsername(Authentication authentication);

    UserInfoDto getUser(Authentication authentication);

    ResponseCookie signoutUser();
}
