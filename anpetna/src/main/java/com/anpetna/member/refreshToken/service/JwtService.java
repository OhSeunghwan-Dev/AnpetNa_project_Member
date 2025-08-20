package com.anpetna.member.refreshToken.service;

import com.anpetna.member.refreshToken.dto.LoginRequest;
import com.anpetna.member.refreshToken.dto.TokenResponse;

public interface JwtService {
    TokenResponse login(LoginRequest loginRequest);
    TokenResponse refresh(String refreshToken);
}
