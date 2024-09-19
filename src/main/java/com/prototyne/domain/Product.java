package com.prototyne.domain;

import com.prototyne.domain.common.BaseEntity;
import com.prototyne.domain.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(nullable = false, length = 200)
    private String notes;

    @Column(nullable = false, length = 512)
    private String contents;

    private String thumbnailUrl;

    private Integer reqTickets;

    @Column(nullable = true, length = 100)
    private String question1;

    @Column(nullable = true, length = 100)
    private String question2;

    @Column(nullable = true, length = 100)
    private String question3;

    @Column(nullable = true, length = 100)
    private String question4;

    @Column(nullable = true, length = 100)
    private String question5;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> eventList = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> productImageList = new ArrayList<>();
}
