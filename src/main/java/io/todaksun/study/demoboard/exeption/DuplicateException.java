package io.todaksun.study.demoboard.exeption;

import lombok.Getter;

public class DuplicateException extends RuntimeException {

    @Getter
    private final String duplicateValue;

    public DuplicateException(String duplicateValue) {
        this.duplicateValue = duplicateValue;
    }
}
