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

        Product product = productRepository.findByEnterpriseIdAndId(enterpriseId, event.getProduct().getId());
        /*String question1 = product.getQuestion1();
        String question2 = product.getQuestion2();
        String question3 = product.getQuestion3();
        String question4 = product.getQuestion4();
        String question5 = product.getQuestion5();*/
        List<String> questions = Arrays.asList(
                product.getQuestion1(),
                product.getQuestion2(),
                product.getQuestion3(),
                product.getQuestion4()
        );
        String question5 = product.getQuestion5();

        List<EntAllReviewResponseDTO.ObjectDTO> objectives = new ArrayList<>();
        List<EntAllReviewResponseDTO.ImageDTO> images = new ArrayList<>();
        EntAllReviewResponseDTO.SubjectiveDTO subjective = null;

        List<Integer> repurchase = null;

        for (Investment investment : investments) {
            List<Feedback> feedbacks = feedbackRepository.findAllByInvestmentId(investment.getId());
/*
            Map<Integer, Long> question1Count = feedbacks.stream()
                    .filter(feedback -> question1.equals(question1))
                    .map(Feedback::getAnswer1)
                    .collect(Collectors.groupingBy(answer1 -> answer1, Collectors.counting()));

            List<Integer> answers1 = Arrays.asList(
                    question1Count.getOrDefault(1, 0L).intValue(),
                    question1Count.getOrDefault(2, 0L).intValue(),
                    question1Count.getOrDefault(3, 0L).intValue(),
                    question1Count.getOrDefault(4, 0L).intValue()
            );
            EntAllReviewResponseDTO.ObjectDTO objective1 = EntAllReviewResponseDTO.ObjectDTO.builder()
                    .question(question1)
                    .answers(answers1)
                    .build();
            objectives.add(objective1);


            Map<Integer, Long> question2Count = feedbacks.stream()
                    .filter(feedback -> question2.equals(question2))
                    .map(Feedback::getAnswer2)
                    .collect(Collectors.groupingBy(answer2 -> answer2, Collectors.counting()));

            List<Integer> answers2 = Arrays.asList(
                    question2Count.getOrDefault(1, 0L).intValue(),
                    question2Count.getOrDefault(2, 0L).intValue(),
                    question2Count.getOrDefault(3, 0L).intValue(),
                    question2Count.getOrDefault(4, 0L).intValue()
            );
            EntAllReviewResponseDTO.ObjectDTO objective2 = EntAllReviewResponseDTO.ObjectDTO.builder()
                    .question(question2)
                    .answers(answers2)
                    .build();
            objectives.add(objective2);

            Map<Integer, Long> question3Count = feedbacks.stream()
                    .filter(feedback -> question3.equals(question3))
                    .map(Feedback::getAnswer3)
                    .collect(Collectors.groupingBy(answer3 -> answer3, Collectors.counting()));

            List<Integer> answers3 = Arrays.asList(
                    question3Count.getOrDefault(1, 0L).intValue(),
                    question3Count.getOrDefault(2, 0L).intValue(),
                    question3Count.getOrDefault(3, 0L).intValue(),
                    question3Count.getOrDefault(4, 0L).intValue()
            );
            EntAllReviewResponseDTO.ObjectDTO objective3 = EntAllReviewResponseDTO.ObjectDTO.builder()
                    .question(question3)
                    .answers(answers3)
                    .build();
            objectives.add(objective3);

            Map<Integer, Long> question4Count = feedbacks.stream()
                    .filter(feedback -> question4.equals(question4))
                    .map(Feedback::getAnswer1)
                    .collect(Collectors.groupingBy(answer4 -> answer4, Collectors.counting()));

            List<Integer> answers4 = Arrays.asList(
                    question4Count.getOrDefault(1, 0L).intValue(),
                    question4Count.getOrDefault(2, 0L).intValue(),
                    question4Count.getOrDefault(3, 0L).intValue(),
                    question4Count.getOrDefault(4, 0L).intValue()
            );
            EntAllReviewResponseDTO.ObjectDTO objective4 = EntAllReviewResponseDTO.ObjectDTO.builder()
                    .question(question4)
                    .answers(answers4)
                    .build();
            objectives.add(objective4);
            */

            for (int i = 0; i < questions.size(); i++) {
                String question = questions.get(i);

                Map<Integer, Long> answerCount;
                switch (i) {
                    case 0:
                        answerCount = feedbacks.stream()
                                .filter(feedback -> question.equals(product.getQuestion1()))  // 첫 번째 질문
                                .map(Feedback::getAnswer1)  // 첫 번째 답변 필드
                                .collect(Collectors.groupingBy(answer -> answer, Collectors.counting()));
                        break;
                    case 1:
                        answerCount = feedbacks.stream()
                                .filter(feedback -> question.equals(product.getQuestion2()))  // 두 번째 질문
                                .map(Feedback::getAnswer2)  // 두 번째 답변 필드
                                .collect(Collectors.groupingBy(answer -> answer, Collectors.counting()));
                        break;
                    case 2:
                        answerCount = feedbacks.stream()
                                .filter(feedback -> question.equals(product.getQuestion3()))  // 세 번째 질문
                                .map(Feedback::getAnswer3)  // 세 번째 답변 필드
                                .collect(Collectors.groupingBy(answer -> answer, Collectors.counting()));
                        break;
                    case 3:
                        answerCount = feedbacks.stream()
                                .filter(feedback -> question.equals(product.getQuestion4()))  // 네 번째 질문
                                .map(Feedback::getAnswer4)  // 네 번째 답변 필드
                                .collect(Collectors.groupingBy(answer -> answer, Collectors.counting()));
                        break;
                    default:
                        answerCount = new HashMap<>();  // 기본값 처리
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

            Map<Boolean, Long> repurchaseCount = feedbacks.stream()
                    .map(Feedback::getAnswer6)  // 재구매 여부 (true 또는 false)
                    .collect(Collectors.groupingBy(rep -> rep, Collectors.counting()));

            repurchase = Arrays.asList(
                    repurchaseCount.getOrDefault(true, 0L).intValue(),  // true인 경우
                    repurchaseCount.getOrDefault(false, 0L).intValue()  // false인 경우
            );
            for (Feedback feedback : feedbacks) {

                subjective = EntAllReviewResponseDTO.SubjectiveDTO.builder()
                        .question(question5)
                        .answers(EntAllReviewResponseDTO.SubjectiveAnswerDTO.builder()
                                .userId(feedback.getUser().getId())
                                .answer(feedback.getAnswer5())
                                .build())
                        .build();


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
        }

        return EntAllReviewResponseDTO.ReviewResponse.builder()
                .objectives(objectives)
                .subjective(subjective)
                .repurchase(repurchase)
                .images(images)
                .build();
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
