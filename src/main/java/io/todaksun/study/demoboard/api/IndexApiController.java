package io.todaksun.study.demoboard.api;

import io.todaksun.study.demoboard.networks.ResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class IndexApiController {

    @GetMapping
    public ResponseEntity<?> index() {

        log.info(
                "SecurityContextHolder.getContext().getAuthentication().getPrincipal() : {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );


        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseTemplate.create().message("Hello World~!"));

    }

}
