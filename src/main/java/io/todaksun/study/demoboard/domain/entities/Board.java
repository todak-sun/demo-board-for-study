package io.todaksun.study.demoboard.domain.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "createdDateTime",
                           column = @Column(name ="written_date_time"))
})
@EqualsAndHashCode(callSuper = false)
@Builder
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "board_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
}
