package com.movieflix.MovieApi.auth.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    
    @NotBlank(message = "The name field can not be blank")
    private String name;
    
    @NotBlank(message = "The username field can not be blank")
    @Column(unique = true)
    private  String username;
    
    @NotBlank(message = "The email field can not be blank")
    @Column(unique = true)
    @Email(message = "Please enter email in proper format")
    private  String email;
    
    @NotBlank(message = "The password field can not be blank")
    @Size(min = 5 ,message = "Password must be have at least 5 character")
    private  String password;
    
    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;
    
    @OneToOne(mappedBy = "user",cascade = CascadeType.PERSIST)
    private ForgotPassword forgotPassword;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;
    

    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return email;
    }
}
