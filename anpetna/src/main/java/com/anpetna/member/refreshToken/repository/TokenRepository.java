package com.anpetna.member.refreshToken.repository;

import com.anpetna.member.domain.MemberEntity;
import com.anpetna.member.refreshToken.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    @Query("select t from TokenEntity t where t.memberId = :memberId")
    Optional<TokenEntity> findByTokenMemberId(@Param("memberId") String id);

    @Modifying
    @Query("update TokenEntity t set t.revokedAt = CURRENT_TIMESTAMP " +
            "where t.memberId = :memberId and t.revokedAt is null")
    void revokeByMemberId(@Param("memberId") String memberId);
}
