package com.example.demo.auth;

import com.example.demo.eventProducer.UserInfoProducer;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserDetailsServiceImplementation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//configuration because whatever we define in this springboot has to scan that first
//as soon as service start
@Configuration
@EnableMethodSecurity
@Data
public class SecurityConfig {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserDetailsServiceImplementation userDetailsService;

    @Autowired
    private final UserInfoProducer userInfoProducer;

    //we will make constructor because spring security service needs everything
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        return new UserDetailsServiceImplementation(userRepository,passwordEncoder,userInfoProducer);
        //spring ka sec  config jab bhi userdetailservice ko call kregi
        //jo ki h loadbyusername to wo call hoga aur usme jo user repo aur passwordencoder hai
        //use hoga wo ham is class se pass kr rhe hai
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,JwtAuthFilter jwtAuthFilter) throws Exception {
         return http
                 .csrf(AbstractHttpConfigurer::disable).cors(CorsConfigurer::disable)
                 .authorizeHttpRequests(authorizeRequests ->authorizeRequests
                         .requestMatchers("/auth/v1/login","/auth/v1/refreshToken", "/auth/v1/signup").permitAll()
                         .anyRequest().authenticated()
                 )
                 .sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .httpBasic(Customizer.withDefaults())
                 .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                 .authenticationProvider(authenticationProvider())
                 .build();
         //csrf is cross site request forgery when we build our app's frontend
        //agar un 3 api se request ati hai , jwtfilter is never implemented there as it comes only when our token is expired
        //ya user is new thats why dont authenticate these 3 request in rest jwtfilter lgega
        //jo bhi token mil rha hai use stateless rkho store mt kro
        //jb bhi hame user authentication krna hai
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
