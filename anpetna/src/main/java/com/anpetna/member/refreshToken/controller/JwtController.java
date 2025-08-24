package com.anpetna.member.refreshToken.controller;

import com.anpetna.config.JwtProvider;
import com.anpetna.member.domain.MemberEntity;
import com.anpetna.member.dto.loginMember.LoginMemberReq;
import com.anpetna.member.refreshToken.dto.LoginRequest;
import com.anpetna.member.refreshToken.dto.TokenResponse;
import com.anpetna.member.refreshToken.entity.TokenEntity;
import com.anpetna.member.refreshToken.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(MemberEntity memberEntity, @RequestBody LoginMemberReq loginMemberReq) {
        return ResponseEntity.ok(jwtService.login(loginMemberReq,memberEntity));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(String refreshToken, LoginMemberReq loginMemberReq) {
        return ResponseEntity.ok(jwtService.refresh(refreshToken, loginMemberReq));
    }

    @GetMapping("/logout")
    public void logout(String refreshToken, String accessToken, TokenEntity tokenEntity) {
        jwtService.logout(refreshToken, accessToken, tokenEntity);
    }
}
