package com.example.demo.auth;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

//configuration because whatever we define in this springboot has to scan that first
//as soon as service start
@Configuration
@EnableMethodSecurity
@Data
public class SecurityConfig {
}
