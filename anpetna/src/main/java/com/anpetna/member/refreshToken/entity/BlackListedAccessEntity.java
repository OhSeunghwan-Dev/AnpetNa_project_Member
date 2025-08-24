package com.anpetna.member.refreshToken.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "anpetna_access_blacklist")
@Getter
@Setter
@NoArgsConstructor
public class BlackListedAccessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blackno;

    private String accessTokenHash; // 해싱된 access토큰

    private Instant expiresAt;      // 만료시간

}
