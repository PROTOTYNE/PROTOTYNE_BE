package com.prototyne.repository.querydsl;

import com.prototyne.domain.QEvent;
import com.prototyne.domain.QInvestment;
import com.prototyne.domain.QProduct;
import com.prototyne.domain.enums.InvestmentStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TicketRepositoryCustomImpl implements TicketRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    // 유저가 사용한 티켓 계산 (피드백 작성 기준)
    @Override
    public int getUsedTickets(Long userId) {
        QInvestment investment = QInvestment.investment;
        QEvent event = QEvent.event;
        QProduct product = QProduct.product;

        // 피드백 작성 상태의 Investment에 대한 Product의 reqTickets 합산
        Integer usedTickets = jpaQueryFactory.select(product.reqTickets.sum())
                .from(investment)
                .join(investment.event, event)
                .join(event.product, product)
                .where(investment.user.id.eq(userId)
                        .and(investment.status.eq(InvestmentStatus.후기작성)))
                .fetchOne();

        // 결과 반환 (null 체크 포함)
        return usedTickets != null ? usedTickets : 0;
    }

    // 유저의 InvestmentCnt 계산 (피드백 작성 상태 기준)
    @Override
    public int getInvestmentCnt(Long userId) {
        QInvestment investment = QInvestment.investment;

        // 피드백 작성 상태의 Investment 개수 계산
        Long investmentCnt = jpaQueryFactory.select(investment.count())
                .from(investment)
                .where(investment.user.id.eq(userId)
                        .and(investment.status.eq(InvestmentStatus.후기작성)))
                .fetchOne();

        // 결과 반환
        return investmentCnt != null ? investmentCnt.intValue() : 0;
    }
}
