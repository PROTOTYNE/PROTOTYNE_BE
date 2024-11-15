package com.prototyne.repository.querydsl;

import com.prototyne.domain.Event;
import com.prototyne.domain.QEvent;
import com.prototyne.domain.enums.ProductCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class EventRepositoryCustomImpl implements EventRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Event> findByCategory(ProductCategory category){
        QEvent event = QEvent.event;
        return jpaQueryFactory.selectFrom(event)
                .where(event.product.category.eq(category))
                .fetch();
    }
}
