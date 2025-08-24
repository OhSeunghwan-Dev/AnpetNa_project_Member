package com.anpetna.service;

import com.anpetna.config.JwtProvider;
import com.anpetna.member.dto.loginMember.LoginMemberReq;
import com.anpetna.member.refreshToken.dto.TokenResponse;
import com.anpetna.member.refreshToken.entity.TokenEntity;
import com.anpetna.member.refreshToken.repository.TokenRepository;
import com.anpetna.member.refreshToken.service.JwtServiceImpl;
import com.anpetna.member.refreshToken.util.TokenHash;
import com.anpetna.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class TokenServiceTests {

    @InjectMocks
    private JwtServiceImpl jwtService;
    @Mock
    MemberRepository memberRepository = mock(MemberRepository.class);
    @Mock
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    @Mock
    JwtProvider jwtProvider = mock(JwtProvider.class);
    @Mock
    TokenRepository tokenRepository = mock(TokenRepository.class);
    @Mock
    TokenHash tokenHash = mock(TokenHash.class);

    JwtServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new JwtServiceImpl(tokenRepository, jwtProvider, passwordEncoder, tokenHash);
    }

    @Test
    public void loginTest() {
        String memberId = "user01";
        String rawPw = "123456";
        String encPw = "ENC";

        // 요청은 raw 비번으로
        LoginMemberReq req = LoginMemberReq.builder()
                .memberId(memberId)
                .memberPw(rawPw)
                .build();

        // 서비스 구현에 맞춰 TokenEntity or MemberEntity 준비
        TokenEntity te = new TokenEntity();
        te.setId(memberId);
        te.setPw(encPw);

        // findBy... 시그니처 정확히 맞추기 (예: findByToken or findById)
        when(tokenRepository.findByTokenMemberId(eq(memberId)))
                .thenReturn(Optional.of(te));

        // matches(raw, encoded) 순서 주의!
        when(passwordEncoder.matches(eq(rawPw), eq(encPw)))
                .thenReturn(true);

        // 토큰 발급은 mock으로 고정값 반환
        when(jwtProvider.createAccessToken(eq(memberId))).thenReturn("access-123");
        when(jwtProvider.createRefreshToken(eq(memberId))).thenReturn("refresh-456");

        // save는 그대로 돌려주기
        when(tokenRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // when
        TokenResponse res = sut.login(req);

        // then
        assertThat(res.getAccessToken()).isEqualTo("access-123");
        assertThat(res.getRefreshToken()).isEqualTo("refresh-456");

        //리프레시 확인
        ArgumentCaptor<TokenEntity> cap = ArgumentCaptor.forClass(TokenEntity.class);
        verify(tokenRepository).save(cap.capture());
        TokenEntity tokenEntity = cap.getValue();
        assertThat(tokenEntity.getId()).isEqualTo(req.getMemberId());
        verify(passwordEncoder).matches(req.getMemberPw(), encPw);
    }

    @Test
    public void refreshTokenTest() {
        TokenEntity te = new TokenEntity();
        te.setId("memberId");
        te.setAccessToken("Access.Token.123");
        te.setRefreshToken("hashed-old");

        LoginMemberReq loginMemberReq = new LoginMemberReq();
        loginMemberReq.setMemberId("memberId");

        String oldRT = "Refresh.Token.456";
        String newRT = "Refresh.Token.45678";

        when(jwtProvider.validateToken(eq(oldRT))).thenReturn(true);
        when(jwtProvider.getUsername(eq(oldRT))).thenReturn(te.getId());

        when(tokenRepository.findByTokenMemberId(eq(te.getId())))
                .thenReturn(Optional.of(te));

        // 요청 RT 해시 비교 통과
        when(tokenHash.sha256(eq(oldRT))).thenReturn("hashed-old");

        when(jwtProvider.createAccessToken(eq(te.getId()))).thenReturn("Access.Token.12345");
        when(jwtProvider.createRefreshToken(eq(te.getId()))).thenReturn(newRT);

        // 새 RT 저장 시 해시
        when(tokenHash.sha256(eq(newRT))).thenReturn("hashed-new");

        when(tokenRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TokenResponse response = sut.refresh(oldRT, loginMemberReq);

        assertThat(response.getAccessToken()).isEqualTo("Access.Token.12345");
        assertThat(response.getRefreshToken()).isEqualTo(newRT);

        verify(tokenRepository).save(argThat(saved ->
                "hashed-new".equals(saved.getRefreshToken()) &&
                        te.getId().equals(saved.getId())
        ));
    }
}
