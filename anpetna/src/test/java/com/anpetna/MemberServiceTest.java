package com.anpetna;

import com.anpetna.member.domain.MemberEntity;
import com.anpetna.member.dto.MemberDTO;
import com.anpetna.member.repository.WSMemberRepository;
import com.anpetna.member.service.WSMemberService;
import com.anpetna.member.service.WSMemberServiceImpl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private WSMemberServiceImpl memberService;
    @Mock
    private WSMemberRepository memberRepository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    public void joinTest() throws WSMemberService.MemberIdExistException {
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
                .fileName("~~~~")
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
        member.setMemberRole("USER");

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
                .memberroadAddress("경기도")
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
        assertThat(oldMember.getMemberroadAddress()).isEqualTo("한국");
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
                .memberroadAddress("경기도")
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
