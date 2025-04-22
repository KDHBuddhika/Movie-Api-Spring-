package com.movieflix.MovieApi.controller;


import com.movieflix.MovieApi.auth.services.AuthService;
import com.movieflix.MovieApi.auth.utils.AuthResponse;
import com.movieflix.MovieApi.auth.utils.LoginRequest;
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
    
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
     
     return  ResponseEntity.ok(authService.registerUser(registerRequest));
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        
        return  ResponseEntity.ok(authService.login(loginRequest));
    }
    
    
    
    
}
