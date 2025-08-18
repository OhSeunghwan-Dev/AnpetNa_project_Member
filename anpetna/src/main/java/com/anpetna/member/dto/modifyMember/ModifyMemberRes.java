package com.anpetna.member.dto.modifyMember;

import com.anpetna.member.domain.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyMemberRes {

    private String memberId;//아이디
    private String memberPw;//비밀번호
    private String memberEmail;//이메일
    private String memberPhone;//전화번호
    private String memberRoadAddress;//주소
    private String memberZipCode;//우편번호
    private String memberHasPet;//반려동물유무
    private String emailStsYn;
    private String smsStsYn;

    private List memberFileImage;//프로필 사진 이름

    private String etc;

    public static ModifyMemberRes from(MemberEntity memberEntity) {
        //MemberInfoResponse
        return ModifyMemberRes.builder()
                .memberId(memberEntity.getMemberId())
                .memberPw(memberEntity.getMemberPw())
                .memberEmail(memberEntity.getMemberEmail())
                .memberPhone(memberEntity.getMemberPhone())
                .memberRoadAddress(memberEntity.getMemberRoadAddress())
                .memberZipCode(memberEntity.getMemberZipCode())
                .memberHasPet(memberEntity.getMemberHasPet())
                .emailStsYn(memberEntity.getEmailStsYn())
                .smsStsYn(memberEntity.getSmsStsYn())
                .memberFileImage(memberEntity.getImages())
                .etc(memberEntity.getMemberEtc())
                .build();

    }

}
