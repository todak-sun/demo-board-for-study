package io.todaksun.study.demoboard.domain.repositories;

import io.todaksun.study.demoboard.domain.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
}
