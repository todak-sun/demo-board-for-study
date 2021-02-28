package io.todaksun.study.demoboard.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.todaksun.study.demoboard.auth.WhoAmI;
import io.todaksun.study.demoboard.domain.entities.Board;
import io.todaksun.study.demoboard.domain.entities.Member;
import io.todaksun.study.demoboard.exeption.InvalidException;
import io.todaksun.study.demoboard.networks.RequestDto;
import io.todaksun.study.demoboard.networks.ResponseTemplate;
import io.todaksun.study.demoboard.service.BoardService;
import io.todaksun.study.demoboard.util.LocalDateTimeStringSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardApiController {
    private final BoardService boardService;

    @PostMapping
    private ResponseEntity<?> create(@Valid @RequestBody final BoardCreateRequest request,
                                     @WhoAmI final Member member,
                                     Errors errors) {
        if (errors.hasErrors()) throw new InvalidException(errors);
        Board newBoard = boardService.write(request.toEntity(), member);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseTemplate.create(new BoardResponse(newBoard, member)));
    }

    @Getter
    @Setter
    static class BoardResponse {
        private Long id;
        private String title;
        private String content;
        @JsonSerialize(using = LocalDateTimeStringSerializer.class)
        private LocalDateTime writtenAt;
        @JsonSerialize(using = LocalDateTimeStringSerializer.class)
        private LocalDateTime updatedAt;
        private String writtenBy;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean mine;

        public BoardResponse(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writtenAt = board.getCreatedDateTime();
            this.updatedAt = board.getUpdatedDateTime();
            this.writtenBy = board.getWriter().getUsername();
        }

        public BoardResponse(Board board, Member member) {
            this(board);
            if (member != null) {
                this.mine = member.getUsername().equals(board.getWriter().getUsername());
            } else {
                this.mine = false;
            }
        }
    }

    @Getter
    @Setter
    static class BoardCreateRequest implements RequestDto<Board> {
        @NotEmpty
        @Length(min = 1, max = 20)
        private String title;
        @NotEmpty
        @Length(min = 1)
        private String content;

        @Override
        public Board toEntity() {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .build();
        }
    }


    @GetMapping("/{bookId}")
    private ResponseEntity<?> read(@PathVariable Long bookId, @WhoAmI Member member) {

        Board board = boardService.read(bookId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseTemplate
                        .create(new BoardResponse(board, member)));
    }

    @GetMapping
    private ResponseEntity<?> read(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) final Pageable pageable) {

        Page<Board> boards = boardService.readPagedList(pageable);

        List<BoardResponse> boardResponseList = boards.get()
                .map(BoardResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseTemplate.create(
                        boardResponseList,
                        boards.getPageable()));
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> update(@PathVariable Long bookId,
                                    @Valid @RequestBody BoardUpdateRequest request,
                                    Errors errors) {

        if (errors.hasErrors()) throw new InvalidException(errors);

        Board updatedBoard = boardService.update(bookId, request.getTitle(), request.getContent());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.create(new BoardResponse(updatedBoard)));
    }

    @Setter
    @Getter
    static class BoardUpdateRequest {
        @NotEmpty
        @Length(min = 1, max = 20)
        String title;

        @NotEmpty
        @Length(min = 1)
        String content;
    }

    @DeleteMapping("/{bookId}")
    private ResponseEntity<?> delete(@PathVariable Long bookId) {
        boardService.delete(bookId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseTemplate.create().message("정상삭제"));
    }

}
