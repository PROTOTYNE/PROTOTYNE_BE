package com.prototyne.repository.querydsl;

import com.prototyne.domain.Event;
import com.prototyne.domain.QEvent;
import com.prototyne.domain.enums.ProductCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@AllArgsConstructor
public class EventRepositoryCustomImpl implements EventRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Event> findByCategoryNew(ProductCategory category, LocalDate now) {
        QEvent event = QEvent.event;
        return jpaQueryFactory.selectFrom(event)
                .where(event.product.category.eq(category)
                        .and(event.eventStart.loe(now))
                        .and(event.eventEnd.goe(now)))
                .orderBy(event.eventStart.desc())
                .fetch();
    }

    @Override
    public List<Event> findByCategoryPopular(ProductCategory category, LocalDate now) {
        QEvent event = QEvent.event;
        return jpaQueryFactory.selectFrom(event)
                .leftJoin(event.investmentList).fetchJoin()
                .where(event.product.category.eq(category)
                        .and(event.eventStart.loe(now))
                        .and(event.eventEnd.goe(now)))
                .groupBy(event)
                .orderBy(event.investmentList.size().desc())
                .fetch();
    }

    @Override
    public List<Event> findByCategoryLowPrice(ProductCategory category, LocalDate now) {
        QEvent event = QEvent.event;
        return jpaQueryFactory.selectFrom(event)
                .where(event.product.category.eq(category)
                        .and(event.eventStart.loe(now))
                        .and(event.eventEnd.goe(now)))
                .orderBy(event.product.reqTickets.asc())
                .fetch();
    }

    @Override
    public List<Event> findByCategoryHighPrice(ProductCategory category, LocalDate now) {
        QEvent event = QEvent.event;
        return jpaQueryFactory.selectFrom(event)
                .where(event.product.category.eq(category)
                        .and(event.eventStart.loe(now))
                        .and(event.eventEnd.goe(now)))
                .orderBy(event.product.reqTickets.desc())
                .fetch();
    }
}
