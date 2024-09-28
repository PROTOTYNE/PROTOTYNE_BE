package com.prototyne.Enterprise.service.ReviewService;

import com.prototyne.Enterprise.converter.EntReviewConverter;
import com.prototyne.Enterprise.web.dto.EntAllReviewResponseDTO;
import com.prototyne.Enterprise.web.dto.EntReviewDTO;
import com.prototyne.Users.converter.ReviewConverter;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.Users.web.dto.ReviewDTO;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.*;
import com.prototyne.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("enterpriseReviewServiceImpl")
@AllArgsConstructor
public class EntReviewServiceImpl implements EntReviewService {
    private final JwtManager jwtManager;
    private final FeedbackRepository feedbackRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final InvestmentRepository investmentRepository;
    private final ProductRepository productRepository;
    private final EventRepository eventRepository;

    @Override
    public EntAllReviewResponseDTO.ReviewResponse getAllReviews(String token, Long eventId) {
        Long enterpriseId = jwtManager.validateJwt(token);
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.LOGIN_ERROR_ID));
        Event event=eventRepository.findById(eventId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.EVENT_ERROR_ID));

        List<Investment> investments = investmentRepository.findByEventId(eventId);

        List<Long> investmentIds = investments.stream()
                .map(Investment::getId)  // 각 Investment의 id를 추출
                .collect(Collectors.toList());

        List<Feedback> feedbacks = feedbackRepository.findAllByInvestmentIdIn(investmentIds);

        Product product = productRepository.findByEnterpriseIdAndId(enterpriseId, event.getProduct().getId());

        List<String> questions = Arrays.asList(
                product.getQuestion1(),
                product.getQuestion2(),
                product.getQuestion3(),
                product.getQuestion4()
        );
        String question5 = product.getQuestion5();

        List<EntAllReviewResponseDTO.ObjectDTO> objectives = new ArrayList<>();
        List<EntAllReviewResponseDTO.ImageDTO> images = new ArrayList<>();
        List<EntAllReviewResponseDTO.SubjectiveDTO> subjectiveList = new ArrayList<>();

        List<Integer> repurchase = null;

        for (int i = 0; i < questions.size(); i++) {
            String question = questions.get(i);

            Map<Integer, Long> answerCount;
            switch (i) {
                case 0:
                    answerCount = feedbacks.stream()
                            .filter(feedback -> question.equals(product.getQuestion1()))
                            .map(Feedback::getAnswer1)
                            .collect(Collectors.groupingBy(answer -> answer, Collectors.counting()));
                    break;
                case 1:
                    answerCount = feedbacks.stream()
                            .filter(feedback -> question.equals(product.getQuestion2()))
                            .map(Feedback::getAnswer2)
                            .collect(Collectors.groupingBy(answer -> answer, Collectors.counting()));
                    break;
                case 2:
                    answerCount = feedbacks.stream()
                            .filter(feedback -> question.equals(product.getQuestion3()))
                            .map(Feedback::getAnswer3)  // 세 번째 답변 필드
                            .collect(Collectors.groupingBy(answer -> answer, Collectors.counting()));
                    break;
                case 3:
                    answerCount = feedbacks.stream()
                            .filter(feedback -> question.equals(product.getQuestion4()))
                            .map(Feedback::getAnswer4)  // 네 번째 답변 필드
                            .collect(Collectors.groupingBy(answer -> answer, Collectors.counting()));
                    break;
                default:
                    answerCount = new HashMap<>();
            }

            List<Integer> answers = Arrays.asList(
                    answerCount.getOrDefault(1, 0L).intValue(),
                    answerCount.getOrDefault(2, 0L).intValue(),
                    answerCount.getOrDefault(3, 0L).intValue(),
                    answerCount.getOrDefault(4, 0L).intValue(),
                    answerCount.getOrDefault(5, 0L).intValue()
            );

            EntAllReviewResponseDTO.ObjectDTO objective = EntAllReviewResponseDTO.ObjectDTO.builder()
                    .question(question)
                    .answers(answers)
                    .build();
            objectives.add(objective);
        }

        // 재구매 여부 집계
        Map<Boolean, Long> repurchaseCount = feedbacks.stream()
                .map(Feedback::getAnswer6)
                .collect(Collectors.groupingBy(rep -> rep, Collectors.counting()));

        repurchase = Arrays.asList(
                repurchaseCount.getOrDefault(true, 0L).intValue(),  // true인 경우
                repurchaseCount.getOrDefault(false, 0L).intValue()  // false인 경우
        );


        // 주관식 답변 및 이미지 처리
        for (Feedback feedback : feedbacks) {

            EntAllReviewResponseDTO.SubjectiveDTO subjective = EntAllReviewResponseDTO.SubjectiveDTO.builder()
                    .question(question5)
                    .answers(EntAllReviewResponseDTO.SubjectiveAnswerDTO.builder()
                            .userId(feedback.getUser().getId())
                            .answer(feedback.getAnswer5())
                            .build())
                    .build();
            subjectiveList.add(subjective);

            if (feedback.getFeedbackImageList() != null && !feedback.getFeedbackImageList().isEmpty()) {
                // 이미지 URL을 리스트로 변환
                List<String> imageFiles = feedback.getFeedbackImageList().stream()
                        .map(FeedbackImage::getImageUrl)
                        .collect(Collectors.toList());

                EntAllReviewResponseDTO.ImageDTO imageDTO = EntAllReviewResponseDTO.ImageDTO.builder()
                        .userId(feedback.getUser().getId())
                        .imageFiles(imageFiles)
                        .build();

                // images 리스트에 추가
                images.add(imageDTO);
            }
        }

        return EntAllReviewResponseDTO.ReviewResponse.builder()
                .objectives(objectives)
                .subjectiveList(subjectiveList)
                .repurchase(repurchase)
                .images(images)
                .build();

    }


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

    @Override
    public ReviewDTO.ReviewResponseDTO updateReviewQuestion(String token, Long productId, ReviewDTO.ReviewResponseDTO request){
        Long enterpriseId = jwtManager.validateJwt(token);
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
               .orElseThrow(()->new TempHandler(ErrorStatus.LOGIN_ERROR_ID));

        Product product= productRepository.findById(productId).orElseThrow(()->new TempHandler(ErrorStatus.PRODUCT_ERROR_ID));

        product.setQuestion1(request.getQuestion1());
        product.setQuestion2(request.getQuestion2());
        product.setQuestion3(request.getQuestion3());
        product.setQuestion4(request.getQuestion4());
        product.setQuestion5(request.getQuestion5());

        productRepository.save(product);

        return ReviewConverter.toReview(product);

    }
}
