package com.prototyne.domain;

import com.prototyne.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Feedback extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Byte reYn;

    @Column(nullable = true, length = 512)
    private String contents;
    //nvarchar

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean penalty;

    private Integer answer1;

    private Integer answer2;

    private Integer answer3;

    private Integer answer4;

    @Column(nullable = true, length = 100)
    private String answer5;

    private Boolean answer6;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="investment_id")
    private Investment investment;

    @OneToMany(mappedBy ="feedback",cascade=CascadeType.ALL)
    private List<FeedbackImage> feedbackImageList = new ArrayList<>();

}
