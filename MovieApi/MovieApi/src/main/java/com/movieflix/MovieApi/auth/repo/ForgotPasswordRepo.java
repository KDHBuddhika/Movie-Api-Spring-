package com.movieflix.MovieApi.auth.repo;

import com.movieflix.MovieApi.auth.entities.ForgotPassword;
import com.movieflix.MovieApi.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgotPasswordRepo extends JpaRepository<ForgotPassword,Integer> {
    
    Optional<ForgotPassword> findByOtpAndUser(Integer otp , User user);
}
