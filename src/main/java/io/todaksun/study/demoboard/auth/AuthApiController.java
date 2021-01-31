package io.todaksun.study.demoboard.auth;

import io.todaksun.study.demoboard.exeption.SignInFailException;
import io.todaksun.study.demoboard.service.MemberService;
import io.todaksun.study.demoboard.util.JsonWebTokenUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthenticationManager authenticationManager;
    private final MemberService memberService;
    private final JsonWebTokenUtil jsonWebTokenUtil;

    @PostMapping
    public ResponseEntity<?> createAuthToken(@RequestBody AuthRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getUsername(), req.getPassword()
                    )
            );
        } catch (BadCredentialsException exception) {
            throw new SignInFailException();
        }

        final UserDetails userDetails = memberService.loadUserByUsername(req.getUsername());
        final String token = jsonWebTokenUtil.createToken(userDetails);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new AuthResponse(token));
    }

    @RequiredArgsConstructor
    static class AuthResponse {
        @Getter
        private final String token;
    }

    @Getter
    @Setter
    static class AuthRequest {
        private String username;
        private String password;
    }
}
