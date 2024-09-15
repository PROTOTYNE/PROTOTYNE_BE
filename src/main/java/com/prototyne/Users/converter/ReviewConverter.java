package com.prototyne.Users.converter;

import com.prototyne.domain.Product;
import com.prototyne.Users.web.dto.ReviewDTO;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class ReviewConverter {
    public static ReviewDTO.ReviewResponseDTO toReview(Product product) {
        return ReviewDTO.ReviewResponseDTO.builder()
                .id(product.getId())
                .question1(product.getQuestion1())
                .question2(product.getQuestion2())
                .question3(product.getQuestion3())
                .question4(product.getQuestion4())
                .question5(product.getQuestion5())
                .build();

    }
}
