package com.example.demo.controller;

//import authservice.entities.RefreshToken;
//import authservice.model.UserInfoDto;
//import authservice.response.JwtResponseDTO;
//import authservice.service.JwtService;
//import authservice.service.RefreshTokenService;
//import authservice.service.UserDetailsServiceImpl;
import com.example.demo.entities.RefreshToken;
import com.example.demo.model.UserInfoDto;
import com.example.demo.response.JwtResponseDTO;
import com.example.demo.service.JwtService;
import com.example.demo.service.RefreshTokenService;
import com.example.demo.service.UserDetailsServiceImplementation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @PostMapping("auth/v1/signup")//
    public ResponseEntity SignUp(@RequestBody UserInfoDto userInfoDto) {
        try {
            Boolean isSignUped = userDetailsService.signUp(userInfoDto);
            if (Boolean.FALSE.equals(isSignUped)) {
                return new ResponseEntity<>("Already Exist", HttpStatus.BAD_REQUEST);
            }//nj

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.generateToken(userInfoDto.getUsername());
            return new ResponseEntity<>(JwtResponseDTO.builder().accessToken(jwtToken).
                    token(refreshToken.getToken()).build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();  // 📌 PRINT ERROR TO DEBUG
            return new ResponseEntity<>("Exception: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
