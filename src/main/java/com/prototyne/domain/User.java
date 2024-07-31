package com.prototyne.domain;

import com.prototyne.domain.common.BaseEntity;
import com.prototyne.domain.enums.Gender;
import com.prototyne.domain.mapping.Additional;
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
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(columnDefinition = "NVARCHAR(512)")
    private String profileUrl;

    private LocalDateTime birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer tickets;

    private Integer familyMember;

    @OneToMany(mappedBy ="user",cascade=CascadeType.ALL)
    private List<Investment> investmentList= new ArrayList<>();

    @OneToMany(mappedBy ="user",cascade=CascadeType.ALL)
    private List<Heart> heartList= new ArrayList<>();

    @OneToMany(mappedBy ="user",cascade=CascadeType.ALL)
    private List<Ticket> ticketList=new ArrayList<>();

    @OneToMany(mappedBy ="user",cascade=CascadeType.ALL)
    private List<Additional> additionalList= new ArrayList<>();

    @OneToMany(mappedBy ="user",cascade=CascadeType.ALL)
    private List<Alarm> alarmList= new ArrayList<>();

    @OneToMany(mappedBy ="user",cascade=CascadeType.ALL)
    private List<Feedback> feedbackList= new ArrayList<>();
}
