package com.prototyne.converter;

import com.prototyne.domain.Product;
import com.prototyne.web.dto.ReviewDTO;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Builder
public class ProductConverter {
    public static ReviewDTO.ReviewResponseDTO toReview(Product product){
        return ReviewDTO.ReviewResponseDTO.builder()
                .id(product.getId())
                .question1(product.getQuestion1())
                .question2(product.getQuestion2())
                .question3(product.getQuestion3())
                .question4(product.getQuestion4())
                .question5(product.getQuestion5())
                //.question6(request.getQuestion6()) //이미지 첨부 질문
                .build();
    }


}
