package io.todaksun.study.demoboard.exeption;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.validation.FieldError;

@Data
public class ExceptionResponse {

    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object rejectedValue;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String field;

    public ExceptionResponse(FieldError fieldError) {
        this.message = fieldError.getDefaultMessage();
        this.field = fieldError.getField();
        this.rejectedValue = fieldError.getRejectedValue();
    }

    public ExceptionResponse(DuplicateException duplicateException) {
        this.message = "이미 존재하는 값 : " + duplicateException.getDuplicateValue();
    }

    public ExceptionResponse(NotFoundException notFoundException) {
        this.message = "요청한 자원 " + notFoundException.getNotFoundedId() + " 은(는) 찾을 수 없습니다.";
    }

    public ExceptionResponse(SignInFailException signInFailException){
        this.message = "아이디 또는 패스워드가 일치하지 않습니다.";
    }

}
