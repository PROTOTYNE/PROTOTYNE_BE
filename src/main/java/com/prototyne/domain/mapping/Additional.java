package com.prototyne.domain.mapping;

import com.prototyne.domain.ADD_set;
import com.prototyne.domain.User;
import com.prototyne.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Additional extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="set_id")
    private ADD_set addSet;

    public Additional(User user, ADD_set addSet) {
        this.user = user;
        this.addSet = addSet;
    }}
