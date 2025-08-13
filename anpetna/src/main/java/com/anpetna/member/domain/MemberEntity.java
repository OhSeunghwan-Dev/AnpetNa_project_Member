package com.anpetna.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "anpetna_member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberEntity extends BaseEntity{
    @Id
    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(name = "member_pw", nullable = false, length = 40)
    private String memberPw;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_birthY", nullable = false)
    private String memberBirthY;
    @Column(name = "member_birthM", nullable = false)
    private String memberBirthM;
    @Column(name = "member_birthD", nullable = false)
    private String memberBirthD;
    @Column(name = "member_birthGM", nullable = false)
    private String memberBirthGM;

    @Column(name = "member_gender",nullable = false)
    private String memberGender;

    @Column(name = "member_hasPet", nullable = false)
    private String memberHasPet;

    @Column(name = "member_phone", nullable = false)
    private String memberPhone;

    @Column(name = "member_syn", nullable = false)
    private String smsStsYn;

    @Column(name = "member_email", nullable = false)
    private String memberEmail;

    @Column(name = "member_eyn", nullable = false)
    private String emailStsYn;

    @Column(name = "member_roadAddress",nullable = false)
    private String memberRoadAddress;

    @Column(name = "member_zipCode",nullable = false)
    private String memberZipCode;

    @Column(name = "member_detailAddress", nullable = false)
    private String memberDetailAddress;

    @Column(name = "member_role", nullable = false)
    private MemberRole memberRole;

    @Column(name = "member_social")
    private boolean memberSocial;

    @Column(name = "member_etc",length = 3000)
    private String memberEtc;

    @Column(name = "member_fileImage")
    private String memberFileImage;
}
