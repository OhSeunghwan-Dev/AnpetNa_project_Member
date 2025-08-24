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
    private final long accessTokenValidity = 1000 * 60 * 15; // 15분
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24; // 1일

    private final JwtParser strictParser; // 예외를 그대로 던지는 엄격 파서(불변 파서 (thread-safe))

//    생성자에서 secret 주입 → key 생성 → strictParser 생성(초기화 보장)
    public JwtProvider(@Value("${jwt.secret}") String secret) {
        this.strictParser = Jwts.parserBuilder()   // 여기서 초기화 보장
                .setSigningKey(key)//같은 키로 검증
                .build();
    }

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

    public void validateTokenOrThrow(String token) throws JwtException, IllegalArgumentException {
        // parseClaimsJws() 자체가 서명/형식/만료 등을 검증하고,
        // 문제가 있으면 ExpiredJwtException, SignatureException 등을 던집니다.
        strictParser.parseClaimsJws(token);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰이어도 e.getClaims()로 exp/subject 등을 읽을 수 있음
            return e.getClaims();
        }
    }
}

