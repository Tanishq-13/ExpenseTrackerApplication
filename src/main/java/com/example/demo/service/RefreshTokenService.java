package com.example.demo.service;

import com.example.demo.entities.RefreshToken;
import com.example.demo.entities.UserInfo;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        UserInfo userInfo = userRepository.findByUsername(username);
        RefreshToken refreshToken=RefreshToken.builder()
                .userInfo(userInfo)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(6000000))
                .build();

        //save is a default method in crudrepo which will store it in
        //Repository with a ID
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyRefreshToken(RefreshToken refreshToken){
        if(refreshToken.getExpiryDate().compareTo(Instant.now())<0){
            //deletes from repository
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken()+" Refresh Token Expired");
        }
        return refreshToken;
    }

    //repo ko controller me call nhi service me krna chahiye thats just a good practice
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }
}
