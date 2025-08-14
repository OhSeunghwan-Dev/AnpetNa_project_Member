package com.anpetna.member.service;

import com.anpetna.member.domain.MemberEntity;
import com.anpetna.member.dto.MemberDTO;
import com.anpetna.member.dto.readMemberAll.ReadMemberAllReq;
import com.anpetna.member.dto.readMemberAll.ReadMemberAllRes;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {

    public void join(MemberDTO memberDTO) throws MemberIdExistException;

    public MemberEntity readOne(MemberDTO memberDTO);

    ReadMemberAllRes memberReadAll(ReadMemberAllReq readMemberAllReq);

    public void modify(MemberDTO memberDTO);

    public void delete(MemberDTO memberDTO);

    public void chkDuplicatedIt(MemberDTO memberDTO);

    static class MemberIdExistException extends Exception {

    }

}
