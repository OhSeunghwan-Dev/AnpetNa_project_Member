package com.anpetna.member.service;

import com.anpetna.member.domain.MemberEntity;
import com.anpetna.member.dto.MemberDTO;
import com.anpetna.member.repository.WSMemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WSMemberServiceImpl implements WSMemberService {

    private final ModelMapper modelMapper;
    private final WSMemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    // final -> 생성자를 만들고 주입

//    @Autowired
//    public MemberServiceImpl(ModelMapper modelMapper, MemberRepository memberRepository) {
//        this.modelMapper = modelMapper;
//        this.memberRepository = memberRepository;
//    }      ->     @RequiredArgsConstructor

    @Override
    public void join(MemberDTO memberDTO) throws MemberIdExistException{

        String memberId = memberDTO.getMemberId();

        boolean exist = memberRepository.existsById(memberId);

        if (exist){
            throw new MemberIdExistException();
        }

        MemberEntity member = modelMapper.map(memberDTO, MemberEntity.class);
        member.setMemberPw(passwordEncoder.encode(memberDTO.getMemberPw()));
        member.setMemberRole("USER");

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
                .roles(member.getMemberRole())
                .build();
    }

//    @Override
//    public void login(MemberDTO memberDTO) {
//
//    }

    @Override
    public void readOne(MemberDTO memberDTO) {

    }

    @Override
    public void readAll() {

    }

    @Override
    public void modify(MemberDTO memberDTO) {

        String memberId = memberDTO.getMemberId();

        MemberEntity member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            throw new UsernameNotFoundException(memberId);
        }

        member.setMemberPw(passwordEncoder.encode(memberDTO.getMemberPw()));
        member.setMemberPhone(memberDTO.getMemberPhone());
        member.setMemberEmail(memberDTO.getMemberEmail());
        member.setMemberZipCode(memberDTO.getMemberZipCode());
        member.setMemberroadAddress(memberDTO.getMemberRoadAddress());
        member.setMemberEtc(memberDTO.getEtc());

        memberRepository.save(member);
    }

    @Override
    public void delete(MemberDTO memberDTO) {

        String memberId = memberDTO.getMemberId();

        MemberEntity member = memberRepository.findById(memberId).orElse(null);

        memberRepository.delete(member);
    }

    @Override
    public void chkDuplicatedIt(MemberDTO memberDTO) {

    }


}
