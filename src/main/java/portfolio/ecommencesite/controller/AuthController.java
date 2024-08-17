package portfolio.ecommencesite.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import portfolio.ecommencesite.security.jwt.*;
import portfolio.ecommencesite.service.AuthService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = authService.signInUser(loginRequest);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,loginResponse.getResponseCookie().toString()).body(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody @Valid SignUpRequest signUpRequest){

        return ResponseEntity.ok(authService.signUpUser(signUpRequest));
    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        return authService.getUsername(authentication);
    }

    @GetMapping("/user")
    public ResponseEntity<?> currentUser(Authentication authentication){
        return ResponseEntity.ok(authService.getUser(authentication));
    }
    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser(){
        ResponseCookie cookie = authService.signoutUser();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(new MessageResponse("You have been signed out"));
    }

}
