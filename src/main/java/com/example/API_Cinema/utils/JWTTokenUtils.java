package com.example.API_Cinema.utils;

import com.example.API_Cinema.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JWTTokenUtils {

    @Value("${jwt.expiration}")
    private Long expiration; // This should be in seconds

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        //this.generateSecretKey();
        claims.put("phone", user.getPhone());
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhone())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
            throw new InvalidParameterException("Cannot create jwt token: " + e.getMessage());
        }
    }

    private SecretKey getSignKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey); //Keys.hmacShaKeyFor(Decoders.BASE64.decode("d1AhbW9bBYZwXoENsaGu4bgY3+rLo/YRsBnRhrsQPak="))
        return Keys.hmacShaKeyFor(bytes);
    }

    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
        //d1AhbW9bBYZwXoENsaGu4bgY3+rLo/YRsBnRhrsQPak=
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpiration(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String extractPhone(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String phone = extractPhone(token);
        return (phone.equals(userDetails.getUsername())) && !isTokenExpiration(token);
    }
}
