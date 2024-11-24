package com.prototyne.repository.querydsl;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.Event;
import com.prototyne.domain.QEvent;
import com.prototyne.domain.enums.ProductCategory;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    // 홈화면 이벤트 반환
    @Override
    public List<Event> findAllEventsByLimit(String type, int limit) {
        QEvent event = QEvent.event;
        JPAQuery<Event> query = jpaQueryFactory.selectFrom(event);

        // new의 경우 일주일 조건
        if ("new".equals(type)) {
            LocalDate today = LocalDate.now();
            LocalDate lastWeekSameDay = today.minusWeeks(1);

            LocalDateTime startOfLastWeek = lastWeekSameDay.atStartOfDay();             // 시작
            LocalDateTime endOfThisWeek = today.atTime(23, 59, 59);  // 끝

            BooleanExpression weekCondition = event.createdAt.between(startOfLastWeek, endOfThisWeek);
            query.where(weekCondition);
        }

        // 정렬 조건
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(event, type);
        query.orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
                .limit(limit); // 요청된 개수만큼 제한

        return query.fetch();
    }

    // 더보기 이벤트 반환
    @Override
    public List<Event> findAllEventsByType(String type, String cursor, int pageSize) {
        QEvent event = QEvent.event;
        JPAQuery<Event> query = jpaQueryFactory.selectFrom(event);

        // new의 경우 일주일 조건
        if ("new".equals(type)) {
            LocalDate today = LocalDate.now();
            LocalDate lastWeekSameDay = today.minusWeeks(1);

            LocalDateTime startOfLastWeek = lastWeekSameDay.atStartOfDay();             // 시작
            LocalDateTime endOfThisWeek = today.atTime(23, 59, 59);  // 끝

            BooleanExpression weekCondition = event.createdAt.between(startOfLastWeek, endOfThisWeek);
            query.where(weekCondition);
        }

        // 정렬 조건
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(event, type);
        // 커서 조건
        BooleanExpression cursorCondition = getCursorCondition(event, type, cursor);
        query.where(cursorCondition)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
                .limit(pageSize);

        return query.fetch();
    }

    /**
     * 정렬 조건 + 기준을 반환
     */
    private List<OrderSpecifier<?>> getOrderSpecifiers(QEvent event, String type) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        switch (type) {
            case "popular":
                // 인기순: investmentCount 내림차순
                orderSpecifiers.add(event.investmentList.size().desc());
                break;
            case "imminent":
                // 임박순: eventEnd 오름차순
                orderSpecifiers.add(event.eventEnd.asc());
                break;
            case "new":
                // 등록순: createdAt 내림차순
                orderSpecifiers.add(event.createdAt.desc());
                return orderSpecifiers;
            default:
                throw new TempHandler(ErrorStatus.PRODUCT_ERROR_TYPE);
        }

        // 추가 정렬 조건: 등록순 내림차순
        orderSpecifiers.add(event.createdAt.desc());

        return orderSpecifiers;
    }

    /**
     * 커서 조건을 반환
     */
    private BooleanExpression getCursorCondition(QEvent event, String type, String cursor) {
        if (cursor == null) return null; // 첫 요청

        // 커서 값 분리
        String[] cursorValues = cursor.split(",");
        LocalDateTime createdAtCursor = LocalDateTime.parse(cursorValues[cursorValues.length - 1]);

        switch (type) {
            case "popular": {
                // 인기순: investmentCount -> createdAt
                Integer investmentCursor = Integer.parseInt(cursorValues[0]);
                return event.investmentList.size().lt(investmentCursor)
                        .or(event.investmentList.size().eq(investmentCursor)
                                .and(event.createdAt.lt(createdAtCursor)));
            }
            case "imminent": {
                // 임박순: eventEnd -> createdAt
                LocalDate eventEndCursor = LocalDate.parse(cursorValues[0]);
                return event.eventEnd.gt(eventEndCursor)
                        .or(event.eventEnd.eq(eventEndCursor)
                                .and(event.createdAt.lt(createdAtCursor)));
            }
            case "new": {
                // 최신순: createdAt
                return event.createdAt.lt(createdAtCursor);
            }
            default:
                throw new TempHandler(ErrorStatus.PRODUCT_ERROR_TYPE);
        }
    }
}