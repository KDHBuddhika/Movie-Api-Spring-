package com.movieflix.MovieApi.auth.services;


import com.movieflix.MovieApi.auth.entities.RefreshToken;
import com.movieflix.MovieApi.auth.entities.User;
import com.movieflix.MovieApi.auth.repo.RefreshTokenRepo;
import com.movieflix.MovieApi.auth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    
    public RefreshToken createRefreshToken(String username){
        User user = userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with name :"+username));
        
        RefreshToken refreshToken = user.getRefreshToken();
        if(refreshToken == null){
//            long refreshTokenValidity = 5*60*60*10000;
            long refreshTokenValidity = 30 * 1000;
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();
            
            refreshTokenRepo.save(refreshToken);
        }
        
        return refreshToken;
    }
    
    public RefreshToken verifyRefreshToken(String refreshToken){
      
      RefreshToken refreshToken1=  refreshTokenRepo.findByRefreshToken(refreshToken)
              .orElseThrow(() -> new RuntimeException("Refresh token not found"));
      
        if (refreshToken1.getExpirationTime().compareTo(Instant.now()) < 0) {
            refreshTokenRepo.delete(refreshToken1);
            throw new RuntimeException("Refresh Token expired");
        }
    
        return refreshToken1;
    }
    
}
