package com.movieflix.MovieApi.auth.repo;

import com.movieflix.MovieApi.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
