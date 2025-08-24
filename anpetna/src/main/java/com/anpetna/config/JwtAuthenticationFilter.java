package com.anpetna.config;

import com.anpetna.member.refreshToken.service.BlacklistService;
import com.anpetna.member.refreshToken.service.BlacklistServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final BlacklistServiceImpl blacklistService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, BlacklistServiceImpl blacklistService) {
        this.jwtProvider = jwtProvider;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);                      // 토큰 없음 → 익명으로 통과
            return;
        }
        String token = header.substring(7);

        // 2) [중요] 블랙리스트 먼저 확인 → 로그아웃/폐기 토큰 즉시 차단
        if (blacklistService.isBlacklisted(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is revoked");
            return;
        }

        try {
            // 3) 엄격 검증: 만료/서명/형식 오류 시 JwtException 발생
            jwtProvider.validateTokenOrThrow(token);

            // 4) 이미 인증이 있으면 덮어쓰지 않음 (덮어쓰기 방지)
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwtProvider.getUsername(token);

                // 권한이 토큰에 없다면 최소 빈 리스트 사용(null 지양)
                List<GrantedAuthority> authorities = new ArrayList<>();//빈 권한

                var authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);
        } catch (io.jsonwebtoken.JwtException e) {
            // 5) 실패는 명확히 401로 응답하고 체인 중단
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        }
    }
}