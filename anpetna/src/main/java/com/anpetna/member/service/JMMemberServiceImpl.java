package com.anpetna.member.service;

import com.anpetna.member.domain.MemberEntity;
import com.anpetna.member.domain.MemberRole;
import com.anpetna.member.dto.MemberDTO;
import com.anpetna.member.repository.JMMemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class JMMemberServiceImpl implements JMMemberService {

    private final ModelMapper modelMapper;
    private final JMMemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    // final -> 생성자를 만들고 주입

    @Override
    public void join(MemberDTO memberDTO) throws MemberIdExistException{

        String memberId = memberDTO.getMemberId();

        boolean exist = memberRepository.existsById(memberId);

        if (exist){
            throw new MemberIdExistException();
        }

        MemberEntity member = modelMapper.map(memberDTO, MemberEntity.class);
        member.setMemberPw(passwordEncoder.encode(memberDTO.getMemberPw()));
        member.setMemberRole(MemberRole.USER);

        memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        MemberEntity member = memberRepository.findById(memberId).orElse(null);

        if (member == null) {
            throw new UsernameNotFoundException(memberId);
        }

        return User.builder()
                .username(member.getMemberId())
                .password(member.getMemberPw())
                .roles(member.getMemberRole().name())
                .build();
    }

    @Override
    public MemberDTO readOne(String memberId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }

        memberId = authentication.getName();
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
        return MemberDTO.from(member);
    }

    @Override
    public List<MemberEntity> readAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }
        return memberRepository.findAll();
    }


    @Override
    public void modify(MemberDTO memberDTO) {

    }

    @Override
    public void delete(MemberDTO memberDTO) {

    }

    @Override
    public void chkDuplicatedIt(MemberDTO memberDTO) {

    }


}
