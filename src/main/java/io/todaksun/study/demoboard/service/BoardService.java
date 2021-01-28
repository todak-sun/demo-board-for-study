package io.todaksun.study.demoboard.service;

import io.todaksun.study.demoboard.domain.entities.Board;
import io.todaksun.study.demoboard.domain.repositories.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board write(Board newBoard) {
        return boardRepository.save(newBoard);
    }

    public Board read(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("못찾겠다"));
    }

    public Board modify(Long boardId){
        return null;
    }

    public List<Board> readPagedList(Pageable pageable){

        return null;
    }



}
