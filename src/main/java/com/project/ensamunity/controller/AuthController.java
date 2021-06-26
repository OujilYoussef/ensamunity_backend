package com.project.ensamunity.controller;


import com.project.ensamunity.dto.AuthenticationResponse;
import com.project.ensamunity.dto.LoginRequest;
import com.project.ensamunity.dto.RefreshTokenRequest;
import com.project.ensamunity.dto.RegisterRequest;
import com.project.ensamunity.service.AuthService;
import com.project.ensamunity.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
private final RefreshTokenService refreshTokenService;
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount( @PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated succefully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);

    }


    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }
@PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest)
{
    refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
    return  ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully");
}

}