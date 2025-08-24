package com.anpetna.member.refreshToken.service;

import com.anpetna.member.domain.MemberEntity;
import com.anpetna.member.dto.loginMember.LoginMemberReq;
import com.anpetna.member.refreshToken.dto.LoginRequest;
import com.anpetna.member.refreshToken.dto.TokenResponse;
import com.anpetna.member.refreshToken.entity.TokenEntity;

public interface JwtService {
    TokenResponse login(LoginMemberReq loginMemberReq, MemberEntity memberEntity);
    TokenResponse refresh(String refreshToken,LoginMemberReq loginMemberReq);
    void logout(String refreshToken, String accessToken, TokenEntity tokenEntity);
}
