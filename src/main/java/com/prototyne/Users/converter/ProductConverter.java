package com.prototyne.Users.converter;

import com.prototyne.Users.web.dto.UserDto;
import com.prototyne.domain.*;
import com.prototyne.Users.web.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductConverter {

    // 홈 화면 형식
    public static ProductDTO.HomeResponse toHomeResponse (User user,
                                                          List<ProductDTO.EventDTO> pL,
                                                          List<ProductDTO.EventDTO> iL,
                                                          List<ProductDTO.EventDTO> lL) {
        UserDto.UserSpeed userSpeed = UserDto.UserSpeed.builder()
                .username(user.getUsername())
                .profileUrl(user.getProfileUrl())
                .speed(user.getSpeed())
                .build();

        return ProductDTO.HomeResponse.builder()
                .userSpeed(userSpeed)
                .popularList(pL).imminentList(iL).newList(lL).build();
    }

    // 체험 진행 중인 시제품 목록 형식
    public static ProductDTO.EventDTO toEvent(Event event, boolean bookmark) {
        Product product = event.getProduct();
        int investCount = event.getInvestmentList().size();
        LocalDate now = LocalDate.now();

        // 시제품 이미지의 첫번째 이미지
        String productImage = getProductImageUrls(product).stream().findFirst().orElse(null);
        return ProductDTO.EventDTO.builder()
                .eventId(event.getId())
                .proName(product.getName())
                .thumbnailUrl(productImage)
                .entName(product.getEnterprise().getName())
                .reqTickets(product.getReqTickets())
                .bookmark(bookmark)
                .createdAt(event.getCreatedAt())
                .eventEnd(event.getFeedbackEnd())
                .releaseEnd(event.getReleaseEnd())
                .speed(event.getSpeed())
                .investCount(investCount)
                .dDay(calculateDDay(now, event.getEventEnd())) // 디데이 계산
                .build();
    }

    // 검색/카테고리 결과 목록 형식
    public static ProductDTO.SearchResponse toSearch(Event event, Product product,
                                                     int dDay, Boolean isBookmarked) {
        // 시제품 이미지의 첫번째 이미지
        String productImage = getProductImageUrls(product).stream().findFirst().orElse(null);
        return ProductDTO.SearchResponse.builder()
                .id(event.getId())
                .name(product.getName())
                .thumbnailUrl(productImage)
                .dDay(dDay)
                .reqTickets(product.getReqTickets())
                .bookmark(isBookmarked)
                .build();
    }

    // 이벤트 시제품 상세 정보 형식
     public static ProductDTO.EventDetailsResponse toEventDetails(Event event, Investment investment, Boolean isBookmarked ) {
         // 이벤트의 시제품 정보 가져옴
         Product product = event.getProduct();
         // 시제품 이미지 가져옴
         List<String> productImages = getProductImageUrls(product);
         // 날짜 정보 DTO 객체 생성
         ProductDTO.DateInfo dateInfo = toDateInfo(event);
         // 유저 투자 정보 DTO 객체 생성
         ProductDTO.InvestInfo investInfo = toInvestInfo(investment);
         // Heart(북마크)목록에서 제품에 대한 사용자의 북마크 존재여부로 판단

         return  ProductDTO.EventDetailsResponse.builder()
                 .eventId(event.getId())
                 .productId(product.getId())
                 .name(product.getName())
                 .enterprise(product.getEnterprise().getName())
                 .category(product.getCategory())
                 .reqTickets(product.getReqTickets())
                 .imageUrls(productImages)
                 .notes(product.getNotes())
                 .contents(product.getContents())
                 .dateInfo(dateInfo)
                 .investInfo(investInfo)
                 .isBookmarked(isBookmarked)
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
                .build();
    }

    private static ProductDTO.InvestInfo toInvestInfo(Investment investment) {
        if (investment == null) // 모든 필드가 null인 빈 객체를 반환
            return ProductDTO.InvestInfo.builder().build();

        // 후기 작성 안한 경우, penalty null 기본
        Boolean isWritten = (investment.getFeedback() != null) ?
                            investment.getFeedback().getPenalty()
                            : null;

        return ProductDTO.InvestInfo.builder()
                .apply(investment.getApply())
                .status(investment.getStatus())
                .shipping(investment.getShipping())
                .transportNum(investment.getTransportNum())
                .penalty(isWritten)
                .build();
    }

    private static List<String> getProductImageUrls(Product product) {
        return product.getProductImageList().stream()
                .map(ProductImage::getImageUrl) // ProductImage url 추출
                .limit(3)                // 최대 3장으로 제한
                .collect(Collectors.toList());
    }

    // 디데이 계산
    private static Integer calculateDDay(LocalDate now, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(now, endDate);
    }
}
