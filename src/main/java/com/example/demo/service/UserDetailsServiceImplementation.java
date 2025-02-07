package com.example.demo.service;

import com.example.demo.entities.UserInfo;
import com.example.demo.eventProducer.UserInfoProducer;
import com.example.demo.model.UserInfoDto;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
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

    @Autowired
    private final UserInfoProducer userInfoProducer;

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImplementation.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Entering in loadUserByUsername Method...");
        UserInfo userInfo = userRepository.findByUsername(username);

        if (userInfo == null) {
            log.error("Username not found: " + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }
        log.info("User Authenticated Successfully..!!!");
        System.out.println("User found: " + username + ", Enabled: " + userInfo.isEnabled()); // Debug log

        if (!userInfo.isEnabled()) {
            throw new DisabledException("User is disabled: " + username);
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
        userRepository.save(new UserInfo(userId,uid.getUsername(),uid.getPassword(),true,new HashSet<>()));
        //kfka
        //user service se even publish krna hai serialize krke
        //mtlb bytes ke form me
        //fir bytes ko deserialize
        log.debug("Entering in loadUserByUsername Method...");

        userInfoProducer.sendEventToKafka(uid);
        return true;
    }
}
