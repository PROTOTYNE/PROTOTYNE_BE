package com.prototyne.service.FeedbackService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.aws.s3.AmazonS3Manager;
import com.prototyne.converter.FeedbackConverter;
import com.prototyne.converter.FeedbackImageConverter;
import com.prototyne.domain.Feedback;
import com.prototyne.domain.FeedbackImage;
import com.prototyne.domain.Investment;
import com.prototyne.domain.User;
import com.prototyne.repository.*;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.web.dto.FeedbackDTO;
import com.prototyne.web.dto.FeedbackImageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService{
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final InvestmentRepository investmentRepository;
    private final JwtManager jwtManager;
    private final AmazonS3Manager s3Manager;
    //private final UuidRepository uuidRepository;
    //private final FeedbackImageRepository feedbackImageRepository;

    @Override
    public FeedbackDTO UpdateFeedbacks(String accessToken, Long investmentId, FeedbackDTO feedbackDTO){

        Long id = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(id).orElseThrow(() -> new TempHandler(ErrorStatus.LOGIN_ERROR_ID));

        Investment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.INVESTMENT_ERROR_ID));

        Feedback feedback = feedbackRepository.findByInvestmentId(investmentId);

        feedback.setReYn(feedbackDTO.getReYn());
        feedback.setPenalty(feedbackDTO.getPenalty());
        feedback.setAnswer1(feedbackDTO.getAnswer1());
        feedback.setAnswer2(feedbackDTO.getAnswer2());
        feedback.setAnswer3(feedbackDTO.getAnswer3());
        feedback.setAnswer4(feedbackDTO.getAnswer4());
        feedback.setAnswer5(feedbackDTO.getAnswer5());
        feedback.setAnswer6(feedbackDTO.getAnswer6());

        feedbackRepository.save(feedback);

        return FeedbackConverter.toFeedbackDto(feedback,investment);

    }
/*
    public FeedbackImageDTO CreateFeedbacksImage(Long investmentId, FeedbackImageDTO feedbackImageDTO, String accessToken){
        Long userId = jwtManager.validateJwt(accessToken);
        User user=userRepository.findById(userId).orElseThrow(() -> new TempHandler(ErrorStatus.LOGIN_ERROR_ID));

        Investment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.INVESTMENT_ERROR_ID));

        Feedback feedback = feedbackRepository.findByInvestmentId(investmentId);

        // Amazon S3에 이미지 업로드
        List<String> uploadedUrls = AmazonS3Manager.uploadFile("feedback-images", Arrays.asList(feedbackImageDTO.getFeedbackImage()));
        String imageUrl = uploadedUrls.get(0);

        // FeedbackImage 엔티티 생성 및 저장
        FeedbackImage feedbackImage = FeedbackImageConverter.toFeedbackImage(feedbackImageDTO, feedback);
        feedbackImage.setImageUrl(imageUrl);
        feedbackImageRepository.save(feedbackImage);

        // 저장된 FeedbackImage 엔티티를 DTO로 변환하여 반환
        return FeedbackImageConverter.toFeedbackImageDTO(feedbackImage);

    }*/

}
