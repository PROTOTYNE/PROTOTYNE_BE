package com.prototyne.domain;

import com.prototyne.domain.common.BaseEntity;
import com.prototyne.domain.enums.InvestmentShipping;
import com.prototyne.domain.enums.InvestmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Investment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InvestmentStatus status;

    @Enumerated(EnumType.STRING)
    private InvestmentShipping shipping;

    private Boolean apply;

    @Column(nullable = true, length = 20)
    private String transportNum;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="event_id")
    private Event event;

    @OneToMany(mappedBy ="investment",cascade=CascadeType.ALL)
    private List<Feedback> feedbackList=new ArrayList<>();


}
