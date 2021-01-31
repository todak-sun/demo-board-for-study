package io.todaksun.study.demoboard.exeption;

import lombok.Getter;

public class NotFoundException extends RuntimeException {

    @Getter
    private final long notFoundedId;

    public NotFoundException(Long notFoundedId){
        this.notFoundedId = notFoundedId;
    }
}
