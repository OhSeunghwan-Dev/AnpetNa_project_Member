package com.anpetna.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long accessTokenValidity = 1000 * 60 * 30; // 30분
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7일

    public String createAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(key)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

//    private final SecretKey key;
//    private final long validityInMs;
//    private final UserDetailsService userDetailsService;
//
//    public JwtProvider(
//            @Value("${jwt.secret:change-me-please-change-me-please-change-me}") String secret,
//            @Value("${jwt.expiration-ms:3600000}") long validityInMs,
//            UserDetailsService userDetailsService) {
//        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//        this.validityInMs = validityInMs;
//        this.userDetailsService = userDetailsService;
//    }
//
//    public String create(Authentication authentication) {
//        Date now = new Date();
//        Date exp = new Date(now.getTime() + validityInMs);
//
//        return Jwts.builder()
//                .setSubject(authentication.getName())
//                .setIssuedAt(now)
//                .setExpiration(exp)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public boolean validate(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
//
//    public Authentication getAuthentication(String token) {
//        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
//                .parseClaimsJws(token).getBody();
//
//        UserDetails user = userDetailsService.loadUserByUsername(claims.getSubject());
//        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//    }
//    //여기부터 추가
//    // JwtProvider.java (일부)
//    public String createAccessToken(String subject, String rolesCsv, long ttlMs) {
//        Date now = new Date();
//        Date exp = new Date(now.getTime() + ttlMs);
//        return Jwts.builder()
//                .setSubject(subject)
//                .claim("roles", rolesCsv)
//                .claim("typ", "access")                // 유형 표기
//                .setIssuedAt(now).setExpiration(exp)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String createRefreshToken(String subject, long ttlMs) {
//        Date now = new Date();
//        Date exp = new Date(now.getTime() + ttlMs);
//        return Jwts.builder()
//                .setSubject(subject)
//                .claim("typ", "refresh")               // 유형 표기
//                .setIssuedAt(now).setExpiration(exp)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public Claims parse(String token) {
//        return Jwts.parserBuilder().setSigningKey(key).build()
//                .parseClaimsJws(token).getBody();
//    }

}

