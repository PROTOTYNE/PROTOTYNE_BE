package com.prototyne.Enterprise.converter;

import com.prototyne.Enterprise.web.dto.EventDTO;
import com.prototyne.Enterprise.web.dto.ProductDTO;
import com.prototyne.domain.Event;
import com.prototyne.domain.Product;
import com.prototyne.domain.ProductImage;
import com.prototyne.domain.enums.InvestmentStatus;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class EventConverter {
    // 체험 목록 조회 응답 형식으로
    public static EventDTO.EventResponse toEventResponse(Event event, Product product, LocalDate now) {

        int stage = calculateStage(now, event); // 단계 계산
        LocalDate stageDate = calculateStageDate(stage, event); // 단계에 따른 날짜 계산

        // 2. DTO 반환
        return EventDTO.EventResponse.builder()
                .eventId(event.getId())
                .thumbnailUrl(product.getThumbnailUrl())
                .productName(product.getName())
                .stage(stage)
                .stageDate(stageDate)
                .createdDate(event.getCreatedAt().toLocalDate())
                .category(product.getCategory())
                .build();
    }

    // 체험 엔티티 형식으로
    public static Event toEvent(Product product, EventDTO.EventDate request) {
        // 체험 생성 및 반환
        return Event.builder()
                .product(product)
                .eventStart(request.getEventStart())
                .eventEnd(request.getEventEnd())
                .releaseStart(request.getReleaseStart())
                .releaseEnd(request.getReleaseEnd())
                .feedbackStart(request.getFeedbackStart())
                .feedbackEnd(request.getFeedbackEnd())
                .build();
    }

    // 체험 정보 형식으로
    public static EventDTO.EventInfo toEventInfo(Product product, Event event) {
        // 시제품 정보
        ProductDTO.CreateProductRequest.ProductInfo productInfo = ProductDTO.CreateProductRequest.ProductInfo.builder()
                .productName(product.getName())
                .contents(product.getContents())
                .reqTickets(product.getReqTickets())
                .notes(product.getNotes())
                .launchedDate(product.getLaunchedDate())
                .category(product.getCategory())
                .build();

        // 체험 정보
        EventDTO.EventDate eventDate = EventDTO.EventDate.builder()
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .releaseStart(event.getReleaseStart())
                .releaseEnd(event.getReleaseEnd())
                .feedbackStart(event.getFeedbackStart())
                .feedbackEnd(event.getFeedbackEnd())
                .build();

        // DTO 반환
        return EventDTO.EventInfo.builder()
                .productId(product.getId())
                .eventId(event.getId())
                .productImages( // 이미지 url 추출. 없으면 null 반환
                        product.getProductImageList() != null && !product.getProductImageList().isEmpty()
                        ? product.getProductImageList().stream()
                        .map(ProductImage::getImageUrl).collect(Collectors.toList())
                        : null)
                .productInfo(productInfo)
                .dates(eventDate)
                .build();
    }

    // 체험 현황 형식으로
    public static EventDTO.EventProgress toEventProgress(LocalDate now, Event event) {
        // 당첨자 수
        int iCount = (int) event.getInvestmentList().stream()
                .filter(investment -> investment.getStatus() == InvestmentStatus.당첨)
                .count();

        // 후기 작성 수
        int fCount = (int) event.getInvestmentList().stream()
                .filter(investment -> investment.getStatus() == InvestmentStatus.후기작성)
                .count();

        return EventDTO.EventProgress.builder()
                .stage(calculateStage(now, event)).investCount(iCount).feedbackCount(fCount)
                .build();
    }

    // 현재 날짜에 따라 체험(이벤트) 단계 계산
    private static Integer calculateStage(LocalDate now, Event event){
        if (now.isBefore(event.getEventStart())) return 1; // 시작 전
        else if (now.isBefore(event.getReleaseStart())) return 2; // 신청자 모집 기간
        else if (now.isBefore(event.getFeedbackStart())) return 3; // 당첨자 발표 기간
        else if (now.isBefore(event.getFeedbackEnd()) || now.isEqual(event.getFeedbackEnd()))
            return  4; // 후기 작성 기간
        else return 5; // 종료
    }

    // 체험(이벤트) 단계에 따라 단계 종료 날짜 반환
    private static LocalDate calculateStageDate(int stage, Event event) {
        switch (stage) {
            case 1:
                return event.getEventStart(); // 시작 전 -> 이벤트 시작일
            case 2:
                return event.getEventEnd(); // 신청자 모집 기간 -> 이벤트 종료일
            case 3:
                return event.getReleaseEnd(); // 당첨자 발표 기간 -> 발표 종료일
            case 4:
                return event.getFeedbackEnd(); // 후기 작성 기간 -> 작성 종료일
            case 5:
                return event.getFeedbackEnd().plusDays(1); // 종료 -> 종료일 다음날
            default:
                throw new IllegalArgumentException("Invalid stage: " + stage);
        }
    }
}
