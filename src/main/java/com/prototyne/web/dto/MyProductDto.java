package com.prototyne.web.dto;

import com.prototyne.domain.enums.InvestmentShipping;
import com.prototyne.domain.enums.InvestmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class MyProductDto {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class CommonDto {         // 기본 필드 및 전체보기
        private Long investmentId;
        private Long eventId;
        private Long productId;
        private String name;                // 제품 이름 (Product)
        private String thumbnailUrl;        // 제품 썸네일 (Product)
        // private InvestmentStatus status;    // 투자 상태 (Investment)
        private String calculatedStatus;    // 디데이/당첨/미당첨
        private LocalDateTime createdAt;    // 체험 신청일 (createdAt)
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class AppliedDto {    // 신청 중인 체험
        private CommonDto commonInfo;
        private Integer dDayToSelected;    // 당첨 발표일 D-n (releaseStart - 현재날짜)
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class OngoingDto {            // 당첨란 체험
        private CommonDto commonInfo;
        private InvestmentShipping shipping;    // 배송 상태 (Investment)
        private String transportNum;            // 운송장 번호 (Investment)
        private LocalDateTime feedbackStart;    // 후기작성 시작시간 (Event)
        private LocalDateTime feedbackEnd;      // 후기작성 마감시간 (Event)
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ReviewedDto {           // 후기 작성란 체험
        private CommonDto commonInfo;
        private LocalDateTime judgeEnd;         // 심사 마감시간 (Event)
        private Integer dDayToComplete;         // 심사 발표일 D-n (judgeEnd - 현재날짜)
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class CompletedDto {          // 종료란 체험
        private CommonDto commonInfo;
        private boolean penalty;                  // 완료 or 페널티 (Feedback)
    }
}
