package com.prototyne.domain;

import com.prototyne.domain.common.BaseEntity;
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

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String regNumber;

    @Column(nullable = true, length = 20)
    private String phone;

    @Column(nullable = true, length = 20)
    private String email;

    @Column(nullable = true, length = 512)
    private String enterpriseDesc;

    private String thumbnailUrl;

    @OneToMany(mappedBy ="enterprise",cascade=CascadeType.ALL)
    private List<Product> productList=new ArrayList<>();
}
