package com.example.yuvraj.EventTracker.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

    private SecretKey getSigningKey() throws JwtException {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String token) throws JwtException {
                Claims claims = extractAllClaims(token);
                return claims.getSubject();
    }

    public Date extractExpiration(String token) throws JwtException {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) throws JwtException {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    private Boolean isTokenExpired(String token) throws JwtException {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) throws JwtException {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username,1000L * 60 * 5,"access");
    }

    public String generateToken(String username,Long time,String type) throws JwtException {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username,time,type);
    }

    private String createToken(Map<String, Object> claims, String subject,Long time,String type) throws JwtException {
        return Jwts.builder()
                .claims(claims)
                .claim("token_type",type)
                .subject(subject)
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + time)) // 5 minutes expiration time
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token) throws JwtException {

        return !isTokenExpired(token);
    }
}
