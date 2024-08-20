package com.prototyne.domain;

import com.prototyne.domain.common.BaseEntity;
import com.prototyne.domain.enums.Gender;
import com.prototyne.domain.mapping.Additional;
import com.prototyne.domain.mapping.Heart;
import com.prototyne.web.dto.DeliveryDto;
import com.prototyne.web.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = true, length = 512)
    private String profileUrl;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer familyMember;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean signupComplete;

    @Column(length = 20)
    private String deliveryName;

    @Column(length = 20)
    private String deliveryPhone;

    @Column(length = 50)
    private String baseAddress;

    @Column(length = 50)
    private String detailAddress;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> recentSearchList = new ArrayList<>(); // 최근 검색어 10개 저장

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Investment> investmentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Heart> heartList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ticket> ticketList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Additional> additionalList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Alarm> alarmList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Feedback> feedbackList = new ArrayList<>();

    public void setDelivery(DeliveryDto deliveryDto) {
        this.deliveryName = deliveryDto.getDeliveryName();
        System.out.println("deliveryDto.getDeliveryPhone() = " + deliveryDto.getDeliveryPhone());
        this.deliveryPhone = deliveryDto.getDeliveryPhone();
        this.baseAddress = deliveryDto.getBaseAddress();
        this.detailAddress = deliveryDto.getDetailAddress();
    }

    public void setDetail(UserDto.UserDetailRequest userDetailRequest) {
        this.birth = LocalDate.from(userDetailRequest.getBirth());
        this.familyMember = userDetailRequest.getFamilyMember();
        this.gender = userDetailRequest.getGender();
    }
}
