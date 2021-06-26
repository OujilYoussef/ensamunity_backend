package com.project.ensamunity.service;

import com.project.ensamunity.dto.RefreshTokenRequest;
import com.project.ensamunity.exceptions.EnsamunityException;
import com.project.ensamunity.model.RefreshToken;
import com.project.ensamunity.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken=new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);

    }
    void validateRefreshToken(String token)
    {
        refreshTokenRepository.findByToken(token)
        .orElseThrow(()-> new EnsamunityException("Invalid refresh Token"));
    }

    public void deleteRefreshToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }
}
