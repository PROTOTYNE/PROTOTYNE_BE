package com.prototyne.domain;

import com.prototyne.domain.common.BaseEntity;
import com.prototyne.domain.enums.EnterpriseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Enterprise extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String regNumber;

    private String phone;

    private String email;

    private String address;

    private String category;

    private String size;

    @Enumerated(EnumType.STRING)
    private EnterpriseStatus status;

    @OneToMany(mappedBy ="enterprise",cascade=CascadeType.ALL)
    private List<Product> productList=new ArrayList<>();
}
