package com.anpetna.member.dto.loginMember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginMemberRes {

    private String memberId;
    private String memberPw;

}
