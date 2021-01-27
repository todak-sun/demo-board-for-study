package io.todaksun.study.demoboard.domain.repositories;

import io.todaksun.study.demoboard.domain.entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
