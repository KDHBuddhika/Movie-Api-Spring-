package com.movieflix.MovieApi.auth.repo;

import com.movieflix.MovieApi.auth.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String username);
    
    
    @Transactional   //jakartha one
    @Modifying
    @Query("update User u set u.password = ?2 where  u.email = ?1")
    void updatePassword(String email ,String password);
    
}
