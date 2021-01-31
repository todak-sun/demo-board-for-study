package io.todaksun.study.demoboard.exeption;

import lombok.Getter;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;

public class InvalidException extends RuntimeException {

    @Getter
    private final List<FieldError> errors;

    public InvalidException(Errors errors) {
        this.errors = errors.getFieldErrors();
    }

}
