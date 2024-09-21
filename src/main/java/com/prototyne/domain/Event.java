package com.prototyne.domain;

import com.prototyne.domain.common.BaseEntity;
import com.prototyne.domain.mapping.Heart;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate eventStart;

    @Column(nullable = false)
    private LocalDate eventEnd;

    @Column(nullable = false)
    private LocalDate releaseStart;

    @Column(nullable = false)
    private LocalDate releaseEnd;

    @Column(nullable = false)
    private LocalDate feedbackStart;

    @Column(nullable = false)
    private LocalDate feedbackEnd;

    // 심사 중 필드
    @Column(nullable = true)
    private LocalDateTime judgeStart;
    @Column(nullable = true)
    private LocalDateTime judgeEnd;
    @Column(nullable = true)
    private LocalDate endDate;
    //

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @OneToMany(mappedBy ="event",cascade=CascadeType.ALL)
    private List<Investment> investmentList = new ArrayList<>();

    @OneToMany(mappedBy ="event",cascade=CascadeType.ALL)
    private List<Heart> heartList=new ArrayList<>();

}
