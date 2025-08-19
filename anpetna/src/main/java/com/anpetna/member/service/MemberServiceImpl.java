package com.anpetna.member.service;

import com.anpetna.member.constant.MemberRole;
import com.anpetna.member.domain.MemberEntity;
import com.anpetna.member.dto.MemberDTO;
import com.anpetna.member.dto.deleteMember.DeleteMemberReq;
import com.anpetna.member.dto.deleteMember.DeleteMemberRes;
import com.anpetna.member.dto.joinMember.JoinMemberReq;
import com.anpetna.member.dto.joinMember.JoinMemberRes;
import com.anpetna.member.dto.loginMember.LoginMemberReq;
import com.anpetna.member.dto.loginMember.LoginMemberRes;
import com.anpetna.member.dto.modifyMember.ModifyMemberReq;
import com.anpetna.member.dto.modifyMember.ModifyMemberRes;
import com.anpetna.member.dto.readMemberAll.ReadMemberAllReq;
import com.anpetna.member.dto.readMemberAll.ReadMemberAllRes;
import com.anpetna.member.dto.readMemberOne.ReadMemberOneReq;
import com.anpetna.member.dto.readMemberOne.ReadMemberOneRes;
import com.anpetna.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Transactional
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    // final -> 생성자를 만들고 주입

    @Override
    public JoinMemberRes join(JoinMemberReq joinMemberReq) throws MemberIdExistException{

        String memberId = joinMemberReq.getMemberId();

        boolean exist = memberRepository.existsById(memberId);

        if (exist){
            throw new MemberIdExistException();
        }

        MemberEntity member = modelMapper.map(joinMemberReq, MemberEntity.class);
        member.setMemberPw(passwordEncoder.encode(joinMemberReq.getMemberPw()));
        log.info("======================");
        log.info(member.getMemberPw());
        log.info("======================");
        member.setMemberRole(MemberRole.USER);
        log.info("======================");
        log.info(member + "member");
        log.info("======================");
        memberRepository.save(member);
        return JoinMemberRes.from(member);
    }

//    @Override
//    public LoginMemberRes login(LoginMemberReq loginMemberReq) throws UsernameNotFoundException {
//        MemberEntity member = memberRepository.findById(loginMemberReq.getMemberId())
//                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
//
//        if (!passwordEncoder.matches(loginMemberReq.getMemberPw(), member.getMemberPw())) {
//            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
//        }
//
//        return LoginMemberRes.builder()
//                .memberId(member.getMemberId())
//                .memberPw(passwordEncoder.encode(loginMemberReq.getMemberPw()))
//                .build();
//    }

    @Override
    public LoginMemberRes login(LoginMemberReq req) {
        MemberEntity member = memberRepository.findByMemberId((req.getMemberId()));

        if (member == null) {
            throw new RuntimeException("아이디가 존재하지 않습니다.");
        }
        if (!member.getMemberPw().equals(req.getMemberPw())) {
            throw new RuntimeException("비밀번호가 올바르지 않습니다.");
        }
        LoginMemberRes res = new LoginMemberRes();
//        res.setMemberId(member.getMemberId());
//        res.setMemberPw(req.getMemberPw());
        res.setToken(req.getMemberId());
        res.setToken(req.getMemberPw());
        return res;
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
    public ReadMemberOneRes readOne(ReadMemberOneReq readMemberOneReq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }

        String memberId = authentication.getName();
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
        return ReadMemberOneRes.from(member);
    }

    @Override
     public List<ReadMemberAllRes> memberReadAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }

        List<MemberEntity> member = memberRepository.findAll();

        return ReadMemberAllRes.from(member);
    }


    @Override
    public ModifyMemberRes modify(ModifyMemberReq modifyMemberReq) {

        String memberId = modifyMemberReq.getMemberId();

        MemberEntity member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            throw new UsernameNotFoundException(memberId);
        }

        member.setMemberPw(passwordEncoder.encode(modifyMemberReq.getMemberPw()));
        member.setMemberPhone(modifyMemberReq.getMemberPhone());
        member.setMemberEmail(modifyMemberReq.getMemberEmail());
        member.setMemberZipCode(modifyMemberReq.getMemberZipCode());
        member.setMemberRoadAddress(modifyMemberReq.getMemberRoadAddress());
        member.setMemberEtc(modifyMemberReq.getEtc());
        member.setMemberHasPet(modifyMemberReq.getMemberHasPet());
        member.setImages(modifyMemberReq.getMemberFileImage());

        memberRepository.save(member);

        return ModifyMemberRes.from(member);

    }

    @Override
    public DeleteMemberRes delete(DeleteMemberReq deleteMemberReq) {

        String memberId = deleteMemberReq.getMemberId();

        MemberEntity member = memberRepository.findById(memberId).orElse(null);

        memberRepository.delete(member);
        return null;

    }




}
