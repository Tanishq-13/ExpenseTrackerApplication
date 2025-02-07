package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.apache.catalina.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


//builder chains User.getbuilder().username("a").password("b").build()
//this is same as User u1=new User("a","b") and I will have a constructor that will initialize
//with the value i give
//thats why @Builder is good
@Service
public class JwtService {

    public static final String SECRET="357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    //1. I gotta extract username so i got a token and I made a claim out of it
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    //2. I went to this and passed token in this
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims=extractAllClaims(token);

        //<t> means i can pass any datatype string,int etc
        //<T> means that i can represent a generic in T now i am passing function whose first argument is
        //Claims and next can be anything thats why generic
        //Whenever we gotta pass function then its called functional interface(fi)
        //Its a interface which will always have 1 method
        //Function<T,R> these two are generics
        //Function has apply method which is applied on T and returns R
        //so alternatively i can write public String extractClaim(String token, Function<Claims, String>
        return claimsResolver.apply(claims);
    }

    //3. then I did chaining
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails user) {
        final String username=extractUsername(token);
        return user.getUsername().equals(username) && !isTokenExpired(token);
    }
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

    }
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

}
