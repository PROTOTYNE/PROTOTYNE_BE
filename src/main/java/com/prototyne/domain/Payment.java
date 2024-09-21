package com.prototyne.domain;

import com.prototyne.domain.common.BaseEntity;
import com.prototyne.domain.enums.PaymentStatus;
import com.prototyne.domain.enums.TicketOption;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column
    private String partnerOrderId;

    @Column
    private String tid; //kakaoPay 연동 주문(결제) 고유 id

    @Column(nullable = false, columnDefinition = "결제대기")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // 결제 대기 -> 결제 진행 -> 결제 성공 / 결제 실패

    @Column
    private String productName; //결제 상품 이름

    @Column(nullable = false)
    private int quantity; // 결제 요청 1번 당 수량은 1개여야만 함

    @Column(nullable = false)
    private int amount; // 결제 금액

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketOption ticketOption; // 구매한 티켓 옵션
}
