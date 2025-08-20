package com.anpetna.member.refreshToken.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "anpetna_token")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    @Column(unique = true)
    private String id;
    private String Pw;

    private String RefreshToken;
}