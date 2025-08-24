package com.anpetna.member.refreshToken.service;

import com.anpetna.member.dto.loginMember.LoginMemberReq;
import com.anpetna.member.refreshToken.dto.LoginRequest;
import com.anpetna.member.refreshToken.dto.TokenResponse;

public interface JwtService {
    TokenResponse login(LoginMemberReq loginMemberReq);
    TokenResponse refresh(String refreshToken,LoginMemberReq loginMemberReq);
}
