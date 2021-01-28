package io.todaksun.study.demoboard.api;

import io.todaksun.study.demoboard.domain.entities.Member;
import io.todaksun.study.demoboard.domain.repositories.MemberRepository;
import io.todaksun.study.demoboard.networks.MemberResponse;
import io.todaksun.study.demoboard.networks.RequestDto;
import io.todaksun.study.demoboard.networks.ResponseTemplate;
import io.todaksun.study.demoboard.service.MemberService;
import io.todaksun.study.demoboard.util.validator.Password;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign-in")
public class SignApiController {

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<?> enroll(@Valid @RequestBody final MemberSignRequest request,
                                    Errors errors) {
        if (errors.hasErrors())
            throw new RuntimeException("뭔가 문제가 있다");

        if (request.isValid())
            throw new RuntimeException("너도 그렇다.");

        Member newMember = memberService.signIn(request.toEntity());

        //TODO: 헤더 추가.
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseTemplate.create(new MemberResponse(newMember)));
    }

    @Data
    static class MemberSignRequest implements RequestDto<Member> {
        @Email
        private String username;
        @Password
        @NotEmpty
        private String password;
        @Password
        @NotEmpty
        private String passwordRe;

        public boolean isValid() {
            return !password.equals(passwordRe);
        }

        @Override
        public Member toEntity() {
            return Member.builder()
                    .username(username)
                    .password(password)
                    .build();
        }
    }


}
