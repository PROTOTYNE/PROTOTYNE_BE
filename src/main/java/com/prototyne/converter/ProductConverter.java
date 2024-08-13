package com.prototyne.converter;

import com.prototyne.domain.Event;
import com.prototyne.domain.Feedback;
import com.prototyne.domain.Investment;
import com.prototyne.domain.Product;
import com.prototyne.web.dto.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {

    // 체험 진행 중인 시제품 목록 형식
    public static ProductDTO.EventResponse toEvent(Event event, Product product, int investCount) {
        return ProductDTO.EventResponse.builder()
                .id(event.getId())
                .name(product.getName())
                .thumbnailUrl(product.getThumbnailUrl())
                .investCount(investCount)
                .reqTickets(product.getReqTickets())
                .build();
    }

    // 검색/카테고리 결과 목록 형식
    public static ProductDTO.SearchResponse toSearch(Event event, Product product, int dDay) {
        return ProductDTO.SearchResponse.builder()
                .id(event.getId())
                .name(product.getName())
                .thumbnailUrl(product.getThumbnailUrl())
                .dDay(dDay)
                .reqTickets(product.getReqTickets())
                .build();
    }

    // 이벤트 시제품 상세 정보 형식
     public static ProductDTO.EventDetailsResponse toEventDetails(Event event, Investment investment) {
         // 이벤트의 시제품 정보 가져옴
         Product product = event.getProduct();
         // 날짜 정보 DTO 객체 생성
         ProductDTO.DateInfo dateInfo = toDateInfo(event);
         // 유저 투자 정보 DTO 객체 생성
         ProductDTO.InvestInfo investInfo = toInvestInfo(investment);

         return  ProductDTO.EventDetailsResponse.builder()
                 .id(event.getId())
                 .name(product.getName())
                 .enterprise(product.getEnterprise().getName())
                 .category(product.getCategory())
                 .reqTickets(product.getReqTickets())
                 .dateInfo(dateInfo)
                 .investInfo(investInfo)
                 .build();
     }

    private static ProductDTO.DateInfo toDateInfo(Event event) {
        return ProductDTO.DateInfo.builder()
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .releaseStart(event.getReleaseStart())
                .releaseEnd(event.getReleaseEnd())
                .feedbackStart(event.getFeedbackStart())
                .feedbackEnd(event.getFeedbackEnd())
                .judgeStart(event.getJudgeStart())
                .judgeEnd(event.getJudgeEnd())
                .endDate(event.getEndDate())
                .build();
    }

    private static ProductDTO.InvestInfo toInvestInfo(Investment investment) {
        if (investment == null) // 모든 필드가 null인 빈 객체를 반환
            return ProductDTO.InvestInfo.builder().build();

        // 가장 첫 번째 Feedback의 penalty 값을 가져오는 예시
        Boolean penalty = investment.getFeedbackList().stream()
                .map(Feedback::getPenalty)
                .findFirst().orElse(null);

        return ProductDTO.InvestInfo.builder()
                .apply(investment.getApply())
                .status(investment.getStatus())
                .shipping(investment.getShipping())
                .transportNum(investment.getTransportNum())
                .penalty(penalty)
                .build();
    }
}
