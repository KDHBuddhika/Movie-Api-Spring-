package com.movieflix.MovieApi.controller;


import com.movieflix.MovieApi.auth.entities.RefreshToken;
import com.movieflix.MovieApi.auth.entities.User;
import com.movieflix.MovieApi.auth.services.AuthService;
import com.movieflix.MovieApi.auth.services.JwtService;
import com.movieflix.MovieApi.auth.services.RefreshTokenService;
import com.movieflix.MovieApi.auth.utils.AuthResponse;
import com.movieflix.MovieApi.auth.utils.LoginRequest;
import com.movieflix.MovieApi.auth.utils.RefreshTokenRequest;
import com.movieflix.MovieApi.auth.utils.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private RefreshTokenService refreshTokenService;
    
    @Autowired
    private JwtService jwtService;
    
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
     
     return  ResponseEntity.ok(authService.registerUser(registerRequest));
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        
        return  ResponseEntity.ok(authService.login(loginRequest));
    }
    
    
    @PostMapping("/refresh")
    public  ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();
        
        String accessToken = jwtService.generateToken(user);
        
        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build());
    }
    
}
