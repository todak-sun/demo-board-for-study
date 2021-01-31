package io.todaksun.study.demoboard.service;

import io.todaksun.study.demoboard.domain.entities.Board;
import io.todaksun.study.demoboard.domain.entities.Member;
import io.todaksun.study.demoboard.domain.repositories.BoardRepository;
import io.todaksun.study.demoboard.exeption.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board write(Board newBoard, Member writer) {
        newBoard.writtenBy(writer);
        return boardRepository.save(newBoard);
    }

    public Board read(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(boardId));
    }

    public Page<Board> readPagedList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Board update(Long boardId, String title, String content) {
        return boardRepository.findById(boardId)
                .map(board -> {
                    board.updateContent(title, content);
                    return board;
                })
                .orElseThrow(() -> {
                    throw new NotFoundException(boardId);
                });
    }

    @Transactional
    public void delete(Long boardId) {
        boardRepository.findById(boardId).ifPresentOrElse(
                boardRepository::delete,
                () -> {
                    throw new NotFoundException(boardId);
                });
    }

}
