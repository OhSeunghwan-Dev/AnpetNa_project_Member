package com.anpetna.member.dto.logoutMember;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogoutMemberReq {
    private String token;
}
