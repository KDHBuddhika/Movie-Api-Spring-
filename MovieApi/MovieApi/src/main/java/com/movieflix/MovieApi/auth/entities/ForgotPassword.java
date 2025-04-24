package com.movieflix.MovieApi.auth.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ForgotPassword {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    
    @Column(nullable = false)
    private Integer otp;
    
    @Column(nullable = false)
    private Date expirationDate;
    
    @OneToOne
    private User user;
}
