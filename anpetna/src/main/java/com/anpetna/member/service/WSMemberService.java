package com.anpetna.member.service;

import com.anpetna.member.dto.MemberDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface WSMemberService extends UserDetailsService {

    public void join(MemberDTO memberDTO) throws MemberIdExistException;

//    public void login(MemberDTO memberDTO);

    public void readOne(MemberDTO memberDTO);

    public void readAll();

    public void modify(MemberDTO memberDTO);

    public void delete(MemberDTO memberDTO);

    public void chkDuplicatedIt(MemberDTO memberDTO);

    static class MemberIdExistException extends Exception {

    }

}
