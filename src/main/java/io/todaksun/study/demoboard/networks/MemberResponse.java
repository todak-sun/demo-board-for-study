package io.todaksun.study.demoboard.networks;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.todaksun.study.demoboard.domain.entities.Member;
import io.todaksun.study.demoboard.util.LocalDateTimeStringSerializer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponse {
    private final String username;
    @JsonSerialize(using = LocalDateTimeStringSerializer.class)
    private final LocalDateTime enrolledAt;

    public MemberResponse(Member member) {
        this.username = member.getUsername();
        this.enrolledAt = member.getCreatedDateTime();
    }


}
