package com.movieflix.MovieApi.controller;


import com.movieflix.MovieApi.auth.entities.ForgotPassword;
import com.movieflix.MovieApi.auth.entities.User;
import com.movieflix.MovieApi.auth.repo.ForgotPasswordRepo;
import com.movieflix.MovieApi.auth.repo.UserRepo;
import com.movieflix.MovieApi.auth.utils.ChangePassword;
import com.movieflix.MovieApi.dto.MailBody;
import com.movieflix.MovieApi.service.EmailService;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ForgotPasswordRepo forgotPasswordRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    //send mail for email verification
    @PostMapping("/verifyEmail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Please enter valied email "));
        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("OTP for forget password request")  // Add subject
                .text("This is the otp for your forget password request : "+otp)
                .build();
    
        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationDate(new Date(System.currentTimeMillis() + 70 *1000))
                .user(user)
                .build();
        
         emailService.sendSimpleMessage(mailBody);
         forgotPasswordRepo.save(fp);
         
         return ResponseEntity.ok("email sent for verification");
                
                
    }
    
    
    
    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp  , @PathVariable String email){
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Please enter valied email "));
        ForgotPassword fp= forgotPasswordRepo.findByOtpAndUser(otp,user).orElseThrow(() -> new RuntimeException("Invalid OTP for email "+email));
        
        if(fp.getExpirationDate().before(Date.from(Instant.now()))){
            forgotPasswordRepo.deleteById(fp.getId());
            return  new ResponseEntity<>("OTP has Expired" , HttpStatus.EXPECTATION_FAILED);
        }
        
        return ResponseEntity.ok("OTP verified");
    }
    
    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                        @PathVariable String email){
        if(!Objects.equals(changePassword.getPassword() ,changePassword.getRepeatPassword())){
            return new ResponseEntity<>("Please enter the password Again" ,HttpStatus.EXPECTATION_FAILED);
        }
        String encodePassword = passwordEncoder.encode(changePassword.getPassword());
        userRepo.updatePassword(email , encodePassword);
        
        return ResponseEntity.ok("Password has been changed");
    }
    
    
    private Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000,999_999);
        
    }
}
