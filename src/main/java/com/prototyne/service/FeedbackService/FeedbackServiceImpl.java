package com.prototyne.service.FeedbackService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.aws.s3.AmazonS3Manager;
import com.prototyne.converter.FeedbackConverter;
import com.prototyne.domain.Feedback;
import com.prototyne.domain.FeedbackImage;
import com.prototyne.domain.Investment;
import com.prototyne.domain.User;
import com.prototyne.repository.FeedbackImageRepository;
import com.prototyne.repository.FeedbackRepository;
import com.prototyne.repository.InvestmentRepository;
import com.prototyne.repository.UserRepository;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.web.dto.FeedbackDTO;
import com.prototyne.web.dto.FeedbackImageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final InvestmentRepository investmentRepository;
    private final JwtManager jwtManager;
    private final AmazonS3Manager s3Manager;
    private final FeedbackImageRepository feedbackImageRepository;

    @Override
    public FeedbackDTO UpdateFeedbacks(String accessToken, Long investmentId, FeedbackDTO feedbackDTO) {

        Long userId = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new TempHandler(ErrorStatus.LOGIN_ERROR_ID));

        Investment investment = investmentRepository.findFirstByUserIdAndId(userId, investmentId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.INVESTMENT_ERROR_ID));

        Feedback feedback = feedbackRepository.findByInvestmentId(investmentId).orElseGet(() -> Feedback.builder()
                .investment(investment)
                .user(user)
                .reYn((byte) 1)
                .build());

        feedback.setAnswer1(feedbackDTO.getAnswer1());
        feedback.setAnswer2(feedbackDTO.getAnswer2());
        feedback.setAnswer3(feedbackDTO.getAnswer3());
        feedback.setAnswer4(feedbackDTO.getAnswer4());
        feedback.setAnswer5(feedbackDTO.getAnswer5());
        feedback.setAnswer6(feedbackDTO.getAnswer6());

        feedbackRepository.save(feedback);

        return FeedbackConverter.toFeedbackDto(feedback, investment);

    }

    public FeedbackImageDTO CreateFeedbacksImage(String accessToken, Long investmentId, String directory, List<MultipartFile> feedbackImages) {
        Long userId = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new TempHandler(ErrorStatus.LOGIN_ERROR_ID));

        if (feedbackImages.isEmpty() || feedbackImages.size() > 3) {
            throw new TempHandler(ErrorStatus.INVALID_IMAGE_COUNT);
        }
        Investment investment = investmentRepository.findFirstByUserIdAndId(userId, investmentId).orElseThrow(() -> new TempHandler(ErrorStatus.INVESTMENT_ERROR_ID));

        Feedback feedback = feedbackRepository.findByInvestmentId(investmentId).orElseGet(() -> {
            Feedback newFeedback = Feedback.builder()
                    .investment(investment)
                    .user(user)
                    .reYn((byte) 1)
                    .build();

            return feedbackRepository.save(newFeedback);
        });

        // Amazon S3에 이미지 업로드
        List<String> imageUrls = s3Manager.uploadFile(directory, feedbackImages);

        for (String imageUrl : imageUrls) {
            FeedbackImage feedbackImage = FeedbackImage.builder()
                    .imageUrl(imageUrl)
                    .feedback(feedback)
                    .build();

            feedbackImageRepository.save(feedbackImage);

        }

        return FeedbackImageDTO.builder()
                .id(userId)
                .imageUrls(imageUrls)
                .build();


    }

}
