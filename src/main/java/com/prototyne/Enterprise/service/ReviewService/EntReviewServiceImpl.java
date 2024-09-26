package com.prototyne.Enterprise.service.ReviewService;

import com.prototyne.Enterprise.converter.EntReviewConverter;
import com.prototyne.Enterprise.web.dto.EntReviewDTO;
import com.prototyne.apiPayload.config.JwtManager;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.Enterprise;
import com.prototyne.domain.Feedback;
import com.prototyne.domain.FeedbackImage;
import com.prototyne.domain.Investment;
import com.prototyne.repository.EnterpriseRepository;
//import com.prototyne.repository.FeedbackImageRepository;
import com.prototyne.repository.FeedbackRepository;
import com.prototyne.repository.InvestmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("enterpriseReviewServiceImpl")
@AllArgsConstructor
public class EntReviewServiceImpl implements EntReviewService {
    private final JwtManager jwtManager;
    private final FeedbackRepository feedbackRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final InvestmentRepository investmentRepository;
    //private final FeedbackImageRepository feedbackImageRepository;

    @Override
    public List<EntReviewDTO.ReviewResponse> getAllReviews(String token, Long eventId){
        Long enterpriseId = jwtManager.validateJwt(token);
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(()->new TempHandler(ErrorStatus.LOGIN_ERROR_ID));

        List<Investment> investments= investmentRepository.findByEventId(eventId);
        return investments.stream()
                .flatMap(investment -> {
                    List<Feedback> feedbacks = feedbackRepository.findAllByInvestmentId(investment.getId());
                    return feedbacks.stream().map(feedback ->{
                        List<String> imagefiles = new ArrayList<>();
                        if (feedback.getFeedbackImageList() != null &&!feedback.getFeedbackImageList().isEmpty()) {
                            imagefiles = feedback.getFeedbackImageList().stream()
                                   .map(FeedbackImage::getImageUrl)
                                   .collect(Collectors.toList());
                        }
                        return EntReviewConverter.toReviewResponse(feedback, feedback.getUser(), imagefiles);

                    });
                })
                .collect(Collectors.toList());

    }
//EntReviewConverter.toReviewResponse(feedback, feedback.getUser(),imagefiles)
    @Override
    public EntReviewDTO.ReviewResponse getReviewByUserId(String token, Long eventId, Long userId){
        Long enterpriseId = jwtManager.validateJwt(token);
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(()->new TempHandler(ErrorStatus.LOGIN_ERROR_ID));

        Investment investment=investmentRepository.findByEventIdAndUserId(eventId, userId);
        Feedback feedback=feedbackRepository.findByInvestmentIdAndUserId(investment.getId(), userId);
        List<String> imagefiles = new ArrayList<>();

        if (feedback.getFeedbackImageList() != null && !feedback.getFeedbackImageList().isEmpty()) {
            imagefiles = feedback.getFeedbackImageList().stream()
                    .map(FeedbackImage::getImageUrl)
                    .collect(Collectors.toList());



        }
        return EntReviewConverter.toReviewResponse(feedback, feedback.getUser(), imagefiles);
    }

    @Override
    public EntReviewDTO.ReviewResponse updatePenaltyByUserId(String token, Long eventId, Long userId){
        Long enterpriseId = jwtManager.validateJwt(token);
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(()->new TempHandler(ErrorStatus.LOGIN_ERROR_ID));

        Investment investment=investmentRepository.findByEventIdAndUserId(eventId, userId);
        Feedback feedback=feedbackRepository.findByInvestmentIdAndUserId(investment.getId(), userId);
        feedback.setPenalty(true);

        feedbackRepository.save(feedback);

        List<String> imagefiles = new ArrayList<>();

        if (feedback.getFeedbackImageList() != null && !feedback.getFeedbackImageList().isEmpty()) {
            imagefiles = feedback.getFeedbackImageList().stream()
                    .map(FeedbackImage::getImageUrl)
                    .collect(Collectors.toList());



        }
        return EntReviewConverter.toReviewResponse(feedback, feedback.getUser(), imagefiles);
    }
}
