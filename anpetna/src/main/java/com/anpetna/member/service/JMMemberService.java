package com.anpetna.member.service;

import com.anpetna.member.domain.MemberEntity;
import com.anpetna.member.dto.MemberDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface JMMemberService extends UserDetailsService {

    public void join(MemberDTO memberDTO) throws MemberIdExistException;

    public MemberDTO readOne(String memberId);

    public List<MemberEntity> readAll();

    public void modify(MemberDTO memberDTO);

    public void delete(MemberDTO memberDTO);

    public void chkDuplicatedIt(MemberDTO memberDTO);

    static class MemberIdExistException extends Exception {

    }

}
