package io.todaksun.study.demoboard.filter;

import io.todaksun.study.demoboard.service.MemberService;
import io.todaksun.study.demoboard.util.JsonWebTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class JsonWenTokenRequestFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final JsonWebTokenUtil jsonWebTokenUtil;
    private final String AUTH_TYPE = "Bearer ";

    public JsonWenTokenRequestFilter(MemberService memberService, JsonWebTokenUtil jsonWebTokenUtil) {
        this.memberService = memberService;
        this.jsonWebTokenUtil = jsonWebTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader("Authorization");
        log.info("authorization : {}", authorization);
        if (hasAuthorization(authorization)) {
            String jsonWebToken = authorization.substring(AUTH_TYPE.length());
            String username = jsonWebTokenUtil.extractUsername(jsonWebToken);
            log.info("jsonWebToken : {}", jsonWebToken);
            log.info("username : {}", username);
            if (notAuthenticated(username)) {
                log.info("Not Authenticated!!!");
                UserDetails userDetails = memberService.loadUserByUsername(username);
                if (jsonWebTokenUtil.isValidToken(jsonWebToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    log.info("usernamePasswordAuthenticationToken : {}", usernamePasswordAuthenticationToken);

                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean notAuthenticated(String username) {
        return Optional.ofNullable(username).isPresent()
                && Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).isEmpty();
    }

    private boolean hasAuthorization(String authorization) {
        return Optional.ofNullable(authorization).isPresent()
                && authorization.startsWith(AUTH_TYPE);
    }

}
