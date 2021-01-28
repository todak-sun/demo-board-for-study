package io.todaksun.study.demoboard.domain.entities;

import io.todaksun.study.demoboard.util.MemberAdapter;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "tb_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "createdDateTime",
                column = @Column(name = "enrolled_date_time"))
})
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_id")
    private Long id;
    private String username;
    private String password;

    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(this.password);
    }
}
