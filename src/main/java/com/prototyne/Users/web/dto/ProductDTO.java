package com.prototyne.Users.web.dto;

import com.prototyne.domain.enums.InvestmentShipping;
import com.prototyne.domain.enums.InvestmentStatus;
import com.prototyne.domain.enums.ProductCategory;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ProductDTO {

    // 홈 화면 응답 형식
    @Getter
    @Builder
    public static class HomeResponse {
        List<EventResponse> popularList;
        List<SearchResponse> imminentList;
        List<SearchResponse> newList;
    }

    // 시제품 정렬 응답 형식
    @Getter
    @Builder
    public static class EventResponse{
        private Long id;                // 이벤트 시제품 아이디
        private String name;            // 시제품 이름
        private String thumbnailUrl;    // 시제품 썸네일 (첫번째 사진)
        private Integer investCount;    // 신청한 사람 수 -> 투자 테이블 수
        private Integer reqTickets;     // 시제품 필요 티켓 수
        private Boolean bookmark;       // 유저의 북마크 여부
    }

    // 시제품 검색/카테고리 응답 형식
    @Getter
    @Builder
    public static class SearchResponse{
        private Long id;                // 이벤트 시제품 아이디
        private String name;            // 시제품 이름
        private String thumbnailUrl;    // 시제품 썸네일 (첫번째 사진)
        private Integer dDay;           // 디데이(신청마감 기준, 프론트 처리?)
        private Integer reqTickets;     // 시제품 필요 티켓 수
        private Boolean bookmark;       // 북마크 여부
    }

    @Getter
    @Builder
     public static class EventDetailsResponse{
        private Long eventId;           // 이벤트 시제품 아이디
        private Long productId;         // (시)제품 아이디
        private String name;            // 시제품 이름
        private String enterprise;      // 시제품 기업
        private ProductCategory category;   // 시제품 카테고리
        private Integer reqTickets;     // 시제품 필요 티켓 수
        private List<String> imageUrls; // 시제품 이미지(최대 3장)
        private String notes;       // 제공 시제품 및 참고사항
        private String contents;    // 시제품 설명
        private Boolean isBookmarked;   // 사용자의 북마크 여부
        private DateInfo dateInfo;  // 이벤트 날짜 정보
        private InvestInfo investInfo;  // 유저 투자 정보
    }

    @Getter
    @Builder
    public static class DateInfo {
        private LocalDate eventStart;
        private LocalDate eventEnd;
        private LocalDate releaseStart;
        private LocalDate releaseEnd;
        private LocalDate feedbackStart;
        private LocalDate feedbackEnd;
        private LocalDateTime judgeStart;
        private LocalDateTime judgeEnd;
        private LocalDate endDate;
    }

    @Getter
    @Builder
    public static class InvestInfo {
        private Boolean apply;          // 신청 여부
        private InvestmentStatus status;// 투자 상태
        private InvestmentShipping shipping;// 배송상태
        private String transportNum;    // 운송장 번호
        private Boolean penalty;        // 패널티 여부
    }
}
