package com.anpetna.service;

import com.anpetna.member.constant.MemberRole;
import com.anpetna.member.domain.MemberEntity;
import com.anpetna.member.dto.MemberDTO;
import com.anpetna.member.repository.MemberRepository;
import com.anpetna.member.service.MemberService;
import com.anpetna.member.service.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
public class MemberServiceTests {
    @InjectMocks
    private MemberServiceImpl memberService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ModelMapper modelMapper;

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
        MemberRepository mockRepo = Mockito.mock(MemberRepository.class);
        ModelMapper mockMapper = Mockito.mock(ModelMapper.class);
        MemberService service = new MemberServiceImpl(mockMapper, mockRepo, passwordEncoder);

        given(mockRepo.findById("user01"))
                .willReturn(java.util.Optional.of(new MemberEntity("user01","111","test","1999","01","01","M","M","Y","010-0000-0000","Y","ex@ex.com","Y","경기도~~~","00000","ㅇㅇㅇ동ㅎㅎㅎ호", MemberRole.USER,false,"반려동물1마리","~~~")));
        given(mockMapper.map(any(MemberEntity.class), eq(MemberDTO.class)))
                .willAnswer(invocation -> {
                    MemberEntity entity = invocation.getArgument(0);
                    // 직접 변환 메서드 호출 (MemberDTO.from() 가 있다고 가정)
                    return MemberDTO.from(entity);
                });
        // when
        MemberDTO member = service.readOne();
        // then
        assertThat(member.getMemberId()).isEqualTo("user01");
        assertThat(member.getMemberName()).isEqualTo("test");
    }

    @Test
    public void readAllTest() {
        MemberRepository mockRepo = Mockito.mock(MemberRepository.class);
        ModelMapper mockMapper = Mockito.mock(ModelMapper.class);
        MemberService service = new MemberServiceImpl(mockMapper, mockRepo, passwordEncoder);
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

    @Test
    public void joinTest() throws MemberService.MemberIdExistException {
        // given
        MemberDTO member = MemberDTO.builder()
                .memberId("user01")
                .memberPw("12345678")
                .memberName("testUser")
                .memberBirthY("1919")
                .memberBirthM("01")
                .memberBirthD("01")
                .memberEmail("test@test.com")
                .memberGender("M")
                .memberPhone("010-1234-5678")
                .memberZipCode("00000")
                .memberRoadAddress("경기도")
                .social(false)
                .memberHasPet("Y")
                .etc("반려견 1마리 키우는 중")
                .memberFileImage("~~~~~~~")
                .build();

        // stub1
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // stub2
        MemberEntity savedMember = MemberEntity.builder()
                .memberId("user01")
                .memberName("testUser")
                .memberEmail("test@test.com")
                .build();
        when(memberRepository.save(any(MemberEntity.class))).thenReturn(savedMember);

        // stub3
        when(modelMapper.map(any(MemberDTO.class), eq(MemberEntity.class))).thenReturn(
                MemberEntity.builder()
                        .memberId("user01")
                        .memberName("testUser")
                        .memberEmail("test@test.com")
                        .build()
        );

        // when
        memberService.join(member);

        // then
        assertThat(member.getMemberId()).isEqualTo("user01");

    }

    @Test
    void loadUserByUsername_test() {
        // given
        String memberId = "user01";
        MemberEntity member = new MemberEntity();
        member.setMemberId(memberId);
        member.setMemberPw("encodedPassword");
        member.setMemberRole(MemberRole.USER);

        when(memberRepository.findById(memberId)).thenReturn(java.util.Optional.of(member));

        // when
        UserDetails userDetails = memberService.loadUserByUsername(memberId);

        // then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(memberId);
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword");
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER");
    }


    @Test
    public void ModifyTest() {
        // given
        String memberId = "user01";
        MemberDTO member = MemberDTO.builder()
                .memberId(memberId)
                .memberPhone("010-1234-1234")
                .memberEmail("test2@test.com")
                .memberZipCode("00001")
                .memberRoadAddress("한국")
                .etc("반려묘 한마리 추가입니다.")
                .build();

        MemberEntity oldMember = MemberEntity.builder()
                .memberId(memberId)
                .memberPhone("010-1234-5678")
                .memberEmail("test@test.com")
                .memberZipCode("00000")
                .memberRoadAddress("경기도")
                .memberEtc("반려견 1마리 키우는 중")
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(oldMember));
        when(memberRepository.save(any(MemberEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        memberService.modify(member);

        // then
        assertThat(oldMember.getMemberPhone()).isEqualTo("010-1234-1234");
        assertThat(oldMember.getMemberEmail()).isEqualTo("test2@test.com");
        assertThat(oldMember.getMemberZipCode()).isEqualTo("00001");
        assertThat(oldMember.getMemberRoadAddress()).isEqualTo("한국");
        assertThat(oldMember.getMemberEtc()).isEqualTo("반려묘 한마리 추가입니다.");

        verify(memberRepository).findById(memberId);
        verify(memberRepository).save(oldMember);
    }


    @Test
    public void DeleteTest() {
        // given
        MemberEntity member = MemberEntity.builder()
                .memberId("user01")
                .memberPw("12345678")
                .memberName("testUser")
                .memberBirthY("1919")
                .memberBirthM("01")
                .memberBirthD("01")
                .memberEmail("test@test.com")
                .memberGender("M")
                .memberPhone("010-1234-5678")
                .memberZipCode("00000")
                .memberRoadAddress("경기도")
                .memberSocial(false)
                .memberHasPet("Y")
                .memberEtc("반려견 1마리 키우는 중")
                .memberFileImage("~~~~")
                .build();

        MemberDTO memberDTO = MemberDTO.builder()
                .memberId("user01")
                .build();

        memberRepository.save(member);

        // when

        memberService.delete(memberDTO);

        // then

        assertThat(memberRepository.findById("user01")).isEqualTo(Optional.empty());

    }

}

