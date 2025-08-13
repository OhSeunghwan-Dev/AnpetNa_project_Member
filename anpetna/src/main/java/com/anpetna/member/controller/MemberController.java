package com.anpetna.member.controller;

import com.anpetna.member.dto.MemberDTO;
import com.anpetna.member.service.WSMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final WSMemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public void join() {

    }


    @PostMapping("/join")
    public String join(MemberDTO memberDTO) {

        try{
            memberService.join(memberDTO);
        }catch (WSMemberService.MemberIdExistException e){
            return "redirect:/members/join";
        }

        return "redirect:/members"; // 수정

    }


    @GetMapping("/login")
    public void login() {

    }


    @PostMapping("/login")
    public String login(String memberId, String password) {

        try{
            memberService.loadUserByUsername(memberId);
        }catch (Exception e){
            return "redirect:/members/join";
        }
        return "redirect:/members"; // 수정
    }


}
