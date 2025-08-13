package com.anpetna.member.service;

import com.anpetna.member.dto.MemberDTO;
import org.springframework.stereotype.Service;

public interface MemberService {

    public void join(MemberDTO memberDTO);

    public void login(MemberDTO memberDTO);

    public void readOne(MemberDTO memberDTO);

    public void readAll();

    public void modify(MemberDTO memberDTO);

    public void delete(MemberDTO memberDTO);

    public void chkDuplicatedIt(MemberDTO memberDTO);
}
