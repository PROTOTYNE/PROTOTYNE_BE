package com.prototyne.converter;

import com.prototyne.domain.Event;
import com.prototyne.domain.Feedback;
import com.prototyne.domain.Investment;
import com.prototyne.domain.Product;
import com.prototyne.web.dto.MyProductDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Component
public class MyProductConverter {

    public MyProductDto.CommonDto toCommonDto(Investment investment) {
        Event event = investment.getEvent();
        Product product = event.getProduct();

        return MyProductDto.CommonDto.builder()
                .investmentId(investment.getId())
                .eventId(investment.getEvent().getId())
                .productId(investment.getEvent().getProduct().getId())
                .name(product.getName())
                .thumbnailUrl(product.getThumbnailUrl())
                .status(investment.getStatus())
                .createdAt(investment.getCreatedAt())
                .build();
    }

    public MyProductDto.AppliedDto toAppliedDto(Investment investment) {
        MyProductDto.CommonDto commonInfo = toCommonDto(investment);
        LocalDateTime releaseStart = investment.getEvent().getReleaseStart();
        LocalDateTime now = LocalDateTime.now();

        // releaseStart - 현재 날짜
        long dDayToOngoing = ChronoUnit.DAYS.between(now.toLocalDate(), releaseStart.toLocalDate());

        return MyProductDto.AppliedDto.builder()
                .commonInfo(commonInfo)
                .dDayToOngoing((int) dDayToOngoing)
                .build();
    }

    public MyProductDto.OngoingDto toOngoingDto(Investment investment) {
        MyProductDto.CommonDto commonInfo = toCommonDto(investment);

        return MyProductDto.OngoingDto.builder()
                .commonInfo(commonInfo)
                .shipping(investment.getShipping())
                .transportNum(investment.getTransportNum())
                .feedbackStart(investment.getEvent().getFeedbackStart())
                .feedbackEnd(investment.getEvent().getFeedbackEnd())
                .build();
    }

    public MyProductDto.ReviewedDto toReviewedDto(Investment investment) {
        MyProductDto.CommonDto commonInfo = toCommonDto(investment);
        LocalDateTime judgeEnd = investment.getEvent().getJudgeEnd();
        LocalDateTime now = LocalDateTime.now();

        // judgeEnd - 현재 날짜
        long dDayToComplete = ChronoUnit.DAYS.between(now.toLocalDate(), judgeEnd.toLocalDate());

        return MyProductDto.ReviewedDto.builder()
                .commonInfo(commonInfo)
                .judgeEnd(judgeEnd)
                .dDayToComplete((int) dDayToComplete)
                .build();
    }

    public MyProductDto.CompletedDto toCompletedDto(Feedback feedback) {
        Investment investment = feedback.getInvestment();
        MyProductDto.CommonDto commonInfo = toCommonDto(investment);

        return MyProductDto.CompletedDto.builder()
                .commonInfo(commonInfo)
                .penalty(feedback.getPenalty())
                .build();
    }
}
