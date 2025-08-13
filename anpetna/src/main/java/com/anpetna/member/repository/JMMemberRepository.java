package com.anpetna.member.repository;

import com.anpetna.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JMMemberRepository extends JpaRepository<MemberEntity, String> {
}
