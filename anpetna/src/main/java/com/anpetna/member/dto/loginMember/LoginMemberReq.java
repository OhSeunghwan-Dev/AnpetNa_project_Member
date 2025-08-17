package com.anpetna.member.dto.loginMember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginMemberReq {

    private String memberId;
    private String memberPw;

}
