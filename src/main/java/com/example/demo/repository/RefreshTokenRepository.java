package com.example.demo.repository;

import com.example.demo.entities.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {

    //find everything from RefreshToken where everything is token_1
    Optional<RefreshToken> findByToken(String token_1);

}
