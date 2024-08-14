package com.prototyne.web.dto;

import com.prototyne.domain.enums.InvestmentShipping;
import com.prototyne.domain.enums.InvestmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class MyProductDto {
    private Long investmentId;
    private Long eventId;
    private Long productId;
    private String name;                // 제품 이름 (Product)
    private String thumbnailUrl;        // 제품 썸네일 (Product)
    private InvestmentStatus status;    // 투자 상태 (Investment)
    private LocalDateTime createdAt;    // 체험 신청일 (createdAt)

    @Builder
    @Getter
    @AllArgsConstructor
    public static class AppliedDto {
        private Integer dDayOngoing;    // 당첨 발표일 D-n (releaseStart - 현재날짜)
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class OngoingDto {
        private InvestmentShipping shipping;    // 배송 상태 (Investment)
        private String transportNum;            // 운송장 번호 (Investment)
        private LocalDateTime feedbackStart;    // 후기작성 시작시간 (Event)
        private LocalDateTime feedbackEnd;      // 후기작성 마감시간 (Event)
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ReviewedDto {
        private LocalDateTime judgeEnd;         // 심사 마감시간 (Event)
        private Integer dDayComplete;           // 심사 발표일 D-n (judgeEnd - 현재날짜)
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class CompletedDto {
        private String result;                  // 완료 or 페널티 (Feedback)
    }
}
