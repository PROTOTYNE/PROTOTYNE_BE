package com.prototyne.domain.mapping;

import com.prototyne.domain.User;
import com.prototyne.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DeliveryAddress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(length = 20)
    private String deliveryName;  //배송지 이름

    @Column(length = 20)
    private String deliveryPhone; //배송받을 사람 폰 번호

    @Column(length = 20)
    private String postCode; // 우편 번호

    @Column(length = 50)
    private String baseAddress; // 기본 주소

    @Column(length = 50)
    private String detailAddress; // 상세 주소

    @Column(nullable = false)
    private boolean isDefault; // 기본 배송지 여부

}
