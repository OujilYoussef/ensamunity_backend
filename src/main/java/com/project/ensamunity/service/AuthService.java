package com.project.ensamunity.service;

import com.project.ensamunity.dto.AuthenticationResponse;
import com.project.ensamunity.dto.LoginRequest;
import com.project.ensamunity.dto.RefreshTokenRequest;
import com.project.ensamunity.dto.RegisterRequest;
import com.project.ensamunity.exceptions.EnsamunityException;
import com.project.ensamunity.model.NotificationEmail;
import com.project.ensamunity.model.User;
import com.project.ensamunity.model.VerificationToken;
import com.project.ensamunity.repository.UserRepository;
import com.project.ensamunity.repository.VerificationTokenRepository;
import com.project.ensamunity.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    @Transactional
    public void signup(RegisterRequest registerRequest)
    {
        User user=new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);
        String token=generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("PLease activate your account",user.getEmail(),"Thank you for signing up to Spring Reddit, please click on the below url to activate your account : \n  http://localhost:8080/api/auth/accountVerification/" + token));

    }

    private String generateVerificationToken(User user) {
       String token =UUID.randomUUID().toString();
        VerificationToken verificationToken=new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
    Optional<VerificationToken> verificationToken= verificationTokenRepository.findByToken(token);
    verificationToken.orElseThrow(() -> new EnsamunityException("Invalid Token"));
    fetchUserAndEnable(verificationToken.get());
    }

 @Transactional
    public  void fetchUserAndEnable(VerificationToken verificationToken)
    {
      String username = verificationToken.getUser().getUsername();
      User user=userRepository.findByUsername(username) .orElseThrow(()->new EnsamunityException("UserNotFound"+username));
      user.setEnabled(true);
      userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token=jwtProvider.generateToken(authentication);
        return  AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();


    }


    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }


    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
     refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
String token=jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
   return AuthenticationResponse.builder()
    .authenticationToken(token)
    .refreshToken(refreshTokenRequest.getRefreshToken())
    .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
    .username(refreshTokenRequest.getUsername())
    .build();
    }
}
