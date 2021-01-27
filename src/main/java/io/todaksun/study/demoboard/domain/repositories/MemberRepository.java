package io.todaksun.study.demoboard.domain.repositories;

import io.todaksun.study.demoboard.domain.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
