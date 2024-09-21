package com.prototyne.domain;

import com.prototyne.domain.common.BaseEntity;
import com.prototyne.domain.enums.InvestmentShipping;
import com.prototyne.domain.enums.InvestmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Investment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvestmentStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvestmentShipping shipping;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean apply;

    @Column(nullable = true, length = 20)
    private String transportNum;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="event_id")
    private Event event;

    @OneToOne(mappedBy ="investment", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private Feedback feedback;
}
