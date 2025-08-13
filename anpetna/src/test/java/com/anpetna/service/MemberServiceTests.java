package com.anpetna.service;

import com.anpetna.member.domain.MemberEntity;
import com.anpetna.member.domain.MemberRole;
import com.anpetna.member.dto.MemberDTO;
import com.anpetna.member.repository.JMMemberRepository;
import com.anpetna.member.service.JMMemberService;
import com.anpetna.member.service.JMMemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Slf4j
public class MemberServiceTests {
    @Autowired
    private JMMemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setupAuthentication() {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication auth = new UsernamePasswordAuthenticationToken("user01", null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    public void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }


    @Test
    public void readOneTest() {
        // given
        JMMemberRepository mockRepo = Mockito.mock(JMMemberRepository.class);
        ModelMapper mockMapper = Mockito.mock(ModelMapper.class);
        JMMemberService service = new JMMemberServiceImpl(mockMapper, mockRepo, passwordEncoder);

        given(mockRepo.findById("user01"))
                .willReturn(java.util.Optional.of(new MemberEntity("user01","111","test","1999","01","01","M","M","Y","010-0000-0000","Y","ex@ex.com","Y","경기도~~~","00000","ㅇㅇㅇ동ㅎㅎㅎ호", MemberRole.USER,false,"반려동물1마리","~~~")));
        given(mockMapper.map(any(MemberEntity.class), eq(MemberDTO.class)))
                .willAnswer(invocation -> {
                    MemberEntity entity = invocation.getArgument(0);
                    // 직접 변환 메서드 호출 (MemberDTO.from() 가 있다고 가정)
                    return MemberDTO.from(entity);
                });
        // when
        MemberDTO member = service.readOne("user01");
        // then
        assertThat(member.getMemberId()).isEqualTo("user01");
        assertThat(member.getMemberName()).isEqualTo("test");
    }

    @Test
    public void readAllTest() {
        JMMemberRepository mockRepo = Mockito.mock(JMMemberRepository.class);
        ModelMapper mockMapper = Mockito.mock(ModelMapper.class);
        JMMemberService service = new JMMemberServiceImpl(mockMapper, mockRepo, passwordEncoder);
        //given
        List<MemberEntity> memberEntityList = Arrays.asList(
                new MemberEntity("user01","111","test","1999","01","01","M","M","Y","010-0000-0000","Y","ex@ex.com","Y","경기도~~~","00000","ㅇㅇㅇ동ㅎㅎㅎ호",MemberRole.USER,false,"반려동물1마리","~~~"),
                new MemberEntity("user02","111","test","1999","01","01","M","M","Y","010-0000-0000","Y","ex@ex.com","Y","경기도~~~","00000","ㅇㅇㅇ동ㅎㅎㅎ호",MemberRole.USER,false,"반려동물1마리","~~~")
        );
        given(mockRepo.findAll()).willReturn(memberEntityList);
        //when
        List<MemberEntity> memberList = service.readAll();
        //then
        assertThat(memberList).isNotNull().containsExactlyElementsOf(memberEntityList);
    }

}

