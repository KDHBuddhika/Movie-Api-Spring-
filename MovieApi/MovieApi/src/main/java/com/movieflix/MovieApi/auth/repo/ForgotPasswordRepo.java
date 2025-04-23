package com.movieflix.MovieApi.auth.repo;

import com.movieflix.MovieApi.auth.entities.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepo extends JpaRepository<ForgotPassword,Integer> {
}
