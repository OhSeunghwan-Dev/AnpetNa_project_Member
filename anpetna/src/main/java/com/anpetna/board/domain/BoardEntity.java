package com.anpetna.board.domain;

import com.anpetna.board.constant.BoardType;
import com.anpetna.coreDomain.ImageEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor // 모든 필드값으로 생성자 만듬
@NoArgsConstructor // 기본생성자
public class BoardEntity {

    @Column(name = "bWriter", nullable = false)
    private String bWriter;       // 게시물 작성자 -> member 쪽 회원 id fk

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bno", nullable = false)
    private Long bno;            // 게시물 번호

    @Column(name = "bTitle", nullable = false)
    private String bTitle;       // 게시물 제목

    @Lob
    @Column(name = "bContent", nullable = false, length = 2000)
    private String bContent;        // 게시물 내용

    @Column(name = "bViewCount", nullable = false)
    private Integer bViewCount;        // 게시물 조회수

    @Column(name = "bLikeCount", nullable = false)
    private Integer bLikeCount;        // 게시물 좋아요 수

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardType boardType;    // 게시물 종류

    @Column(nullable = false)
    private Boolean noticeFlag;        // 상단 고정 여부

    @Column(nullable = false)
    private Boolean isSecret;        // 비밀글 여부

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageEntity> images = new ArrayList<>(); // 이미지

}