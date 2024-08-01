package com.prototyne.domain;

import com.prototyne.domain.common.BaseEntity;
import com.prototyne.domain.mapping.Heart;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime eventStart;

    private LocalDateTime eventEnd;

    private LocalDateTime releaseStart;

    private LocalDateTime releaseEnd;

    private LocalDateTime feedbackStart;

    private LocalDateTime feedbackEnd;

    private LocalDateTime judgeStart;

    private LocalDateTime judgeEnd;

    private LocalDateTime endDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @OneToMany(mappedBy ="event",cascade=CascadeType.ALL)
    private List<Investment> investmentList = new ArrayList<>();

    @OneToMany(mappedBy ="event",cascade=CascadeType.ALL)
    private List<Heart> heartList=new ArrayList<>();

}
