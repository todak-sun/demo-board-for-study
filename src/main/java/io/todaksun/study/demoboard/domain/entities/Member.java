package io.todaksun.study.demoboard.domain.entities;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tb_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverrides({
        @AttributeOverride(name ="createdDateTime",
                           column = @Column(name ="enrolled_date_time"))
})
@EqualsAndHashCode(callSuper = false)
public class Member extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_id")
    private Long id;
    private String username;
    private String password;
    private String passwordRe;
}
