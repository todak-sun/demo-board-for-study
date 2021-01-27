package io.todaksun.study.demoboard.domain.entities;

import io.todaksun.study.demoboard.domain.repositories.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BoardTest {

    @Autowired
    BoardRepository repository;

    @Test
    @DisplayName("엔티티가 정상적으로 저장된다.")
    public void save_success(){

    }

}