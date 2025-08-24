package com.anpetna.member.refreshToken.repository;

import com.anpetna.member.domain.MemberEntity;
import com.anpetna.member.refreshToken.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByTokenMemberId(String id);
}
