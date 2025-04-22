package com.movieflix.MovieApi.auth.repo;

import com.movieflix.MovieApi.auth.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Integer> {

    
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
