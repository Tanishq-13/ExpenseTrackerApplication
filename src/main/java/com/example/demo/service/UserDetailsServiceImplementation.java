package com.example.demo.service;

import com.example.demo.entities.UserInfo;
import com.example.demo.model.UserInfoDto;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Component
@Data
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userRepository.findByUsername(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException(username);

        }
        return new CustomUserDetails(userInfo);
    }

    public UserInfo checkIfUserExists(UserInfoDto uid) {
        return userRepository.findByUsername(uid.getUsername());
    }

    public Boolean signUp(UserInfoDto uid) {
        uid.setPassword(passwordEncoder.encode(uid.getPassword()));
        if(Objects.nonNull(checkIfUserExists(uid))) {
            return false;
        }
        String userId= UUID.randomUUID().toString();
        userRepository.save(new UserInfo(userId,uid.getUsername(),uid.getPassword(),new HashSet<>()));
        return true;
    }
}
