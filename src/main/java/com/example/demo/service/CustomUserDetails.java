package com.example.demo.service;

import com.example.demo.entities.UserInfo;
import com.example.demo.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


//in short this class brings rows of UserInfo and updates user's username,password etc. save kr leti hai
public class CustomUserDetails extends UserInfo implements UserDetails {
    private String username;

    private String password;

    Collection<? extends GrantedAuthority> authorities;


    public CustomUserDetails(UserInfo byUserName) {
        this.username = byUserName.getUsername();
        this.password = byUserName.getPassword();
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (UserRole role : byUserName.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
