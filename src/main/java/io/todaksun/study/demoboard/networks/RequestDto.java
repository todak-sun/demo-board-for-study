package io.todaksun.study.demoboard.networks;

public interface RequestDto<T> {
    T toEntity();
}
