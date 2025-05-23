package com.prototyne.Users.converter;

import com.prototyne.domain.Event;
import com.prototyne.domain.Feedback;
import com.prototyne.domain.Investment;
import com.prototyne.domain.Product;
import com.prototyne.domain.enums.InvestmentStatus;
import com.prototyne.Users.web.dto.MyProductDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Component
public class MyProductConverter {

    public MyProductDto.CommonDto toCommonDto(Investment investment) {
        Event event = investment.getEvent();
        Product product = event.getProduct();
        LocalDate releaseStart = event.getReleaseDate();
        LocalDate now = LocalDate.now();
        String calculatedStatus;

        if(investment.getStatus()== InvestmentStatus.신청){
            if(releaseStart.isBefore(now)) {
                calculatedStatus = "미당첨";
            } else {
                long dDayToOngoing = (int) ChronoUnit.DAYS.between(now, releaseStart);
                calculatedStatus = String.valueOf(dDayToOngoing);
            }
        } else {
            calculatedStatus = "당첨";
        }

        return MyProductDto.CommonDto.builder()
                .investmentId(investment.getId())
                .eventId(investment.getEvent().getId())
                .productId(investment.getEvent().getProduct().getId())
                .name(product.getName())
                .thumbnailUrl(product.getThumbnailUrl())
                .calculatedStatus(calculatedStatus)
                .createdAt(investment.getCreatedAt())
                .build();
    }

    public MyProductDto.AppliedDto toAppliedDto(Investment investment) {
        MyProductDto.CommonDto commonInfo = toCommonDto(investment);
        LocalDate releaseStart = investment.getEvent().getReleaseDate();
        LocalDateTime now = LocalDateTime.now();

        // releaseStart - 현재 날짜
        long dDayToOngoing = ChronoUnit.DAYS.between(now.toLocalDate(), releaseStart);

        return MyProductDto.AppliedDto.builder()
                .commonInfo(commonInfo)
                .dDayToSelected((int) dDayToOngoing)
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
//        LocalDateTime judgeEnd = investment.getEvent().getJudgeEnd();
        LocalDateTime now = LocalDateTime.now();

        // judgeEnd - 현재 날짜
//        long dDayToComplete = ChronoUnit.DAYS.between(now.toLocalDate(), judgeEnd.toLocalDate());

        return MyProductDto.ReviewedDto.builder()
                .commonInfo(commonInfo)
//                .judgeEnd(judgeEnd)
//                .dDayToComplete((int) dDayToComplete)
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
