package com.anpetna.member.refreshToken.service;

import com.anpetna.config.JwtProvider;
import com.anpetna.member.refreshToken.dto.LoginRequest;
import com.anpetna.member.refreshToken.dto.TokenResponse;
import com.anpetna.member.refreshToken.entity.TokenEntity;
import com.anpetna.member.refreshToken.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse login(LoginRequest loginRequest){
        TokenEntity tokenEntity = tokenRepository.findByToken(loginRequest.getMemberId())
                .orElseThrow(()->new RuntimeException("사용자를 찾을 수가 없습니다"));
        if (!passwordEncoder.matches(loginRequest.getMemberPw(),tokenEntity.getPw())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
        String accessToken = jwtProvider.createAccessToken(tokenEntity.getId());
        String refreshToken = jwtProvider.createRefreshToken(tokenEntity.getId());
        tokenEntity.setRefreshToken(refreshToken);
        tokenRepository.save(tokenEntity);

        return new TokenResponse(accessToken, refreshToken);
    }

    @Override
    public TokenResponse refresh(String refreshToken){
        if(!jwtProvider.validateToken(refreshToken)){
            throw new RuntimeException("무효한 토큰입니다");
        }
        String id = jwtProvider.getUsername(refreshToken);
        TokenEntity tokenEntity = tokenRepository.findByToken(id)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다"));
        if (!refreshToken.equals(tokenEntity.getRefreshToken())){
            throw new RuntimeException("다시 발급받은 토큰이 맞지 않습니다");
        }

        String newAccessToken = jwtProvider.createAccessToken(id);
        String newRefreshToken = jwtProvider.createRefreshToken(id);
        tokenEntity.setRefreshToken(newRefreshToken);
        tokenRepository.save(tokenEntity);

        return new TokenResponse(newAccessToken, newRefreshToken);

    }
}
