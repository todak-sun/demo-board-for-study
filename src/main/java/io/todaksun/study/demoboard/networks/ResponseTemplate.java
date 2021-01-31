package io.todaksun.study.demoboard.networks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.todaksun.study.demoboard.util.LocalDateTimeStringSerializer;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Getter
public class ResponseTemplate<T> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonSerialize(using = LocalDateTimeStringSerializer.class)
    private final LocalDateTime transactionTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Pageable pagination;

    private ResponseTemplate() {
        this.transactionTime = LocalDateTime.now();
    }

    private ResponseTemplate(final T data) {
        this.data = data;
        this.transactionTime = LocalDateTime.now();
    }

    private ResponseTemplate(final T data, Pageable pagination) {
        this.data = data;
        this.pagination = pagination;
        this.transactionTime = LocalDateTime.now();
    }

    public static ResponseTemplate create() {
        return new ResponseTemplate();
    }

    public static <T> ResponseTemplate<T> create(final T data) {
        return new ResponseTemplate<>(data);
    }

    public static <T> ResponseTemplate<T> create(final T data, Pageable pagination) {
        return new ResponseTemplate<>(data, pagination);
    }

    public ResponseTemplate<T> message(String message) {
        this.message = message;
        return this;
    }

}
