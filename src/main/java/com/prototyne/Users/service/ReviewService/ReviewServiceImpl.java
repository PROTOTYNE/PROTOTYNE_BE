package com.prototyne.Users.service.ReviewService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.Event;
import com.prototyne.domain.Product;
import com.prototyne.repository.EventRepository;
import com.prototyne.repository.ReviewRepository;
import com.prototyne.Users.web.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.prototyne.Users.converter.ReviewConverter.toReviewQuestion;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final EventRepository eventRepository;

    @Override
    public ReviewDTO.ReviewQuestionDTO getReviewById(Long eventId) {

        Event event=eventRepository.findById(eventId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.EVENT_ERROR_ID));
        Product product = reviewRepository.findById(event.getProduct().getId())
                .orElseThrow(() -> new TempHandler(ErrorStatus.PRODUCT_ERROR_ID));

        return toReviewQuestion(product,event);
    }

}
