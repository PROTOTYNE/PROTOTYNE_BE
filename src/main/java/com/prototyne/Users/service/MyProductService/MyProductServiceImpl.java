package com.prototyne.Users.service.MyProductService;

import com.prototyne.Users.converter.MyProductConverter;
import com.prototyne.domain.enums.InvestmentStatus;
import com.prototyne.repository.FeedbackRepository;
import com.prototyne.repository.MyProductRepository;
import com.prototyne.apiPayload.config.JwtManager;
import com.prototyne.Users.web.dto.MyProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyProductServiceImpl implements MyProductService {
    private final MyProductRepository myProductRepository;
    private final JwtManager jwtManager;
    private final MyProductConverter myProductConverter;
    private final FeedbackRepository feedbackRepository;

    @Override
    public List<MyProductDto.CommonDto> getAllMyProduct(String accessToken) {
        Long userId = jwtManager.validateJwt(accessToken);

        return myProductRepository.findByUserId(userId).stream()
                .filter(investment -> investment.getApply())
                .map(myProductConverter::toCommonDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<MyProductDto.AppliedDto> getAppliedMyProduct(String accessToken) {
        Long userId = jwtManager.validateJwt(accessToken);
        LocalDate now = LocalDate.now();

        return myProductRepository.findByUserId(userId).stream()
                .filter(investment -> investment.getStatus() == InvestmentStatus.신청)
                .filter(investment -> investment.getEvent().getReleaseStart().isAfter(now))
                .map(myProductConverter::toAppliedDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MyProductDto.OngoingDto> getOngoingMyProduct(String accessToken) {
        Long userId = jwtManager.validateJwt(accessToken);

        return myProductRepository.findByUserId(userId).stream()
                .filter(investment -> investment.getStatus() == InvestmentStatus.당첨)
                .map(myProductConverter::toOngoingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MyProductDto.ReviewedDto> getReviewedMyProduct(String accessToken) {
        Long userId = jwtManager.validateJwt(accessToken);

        return myProductRepository.findByUserId(userId).stream()
                .filter(investment -> investment.getStatus() == InvestmentStatus.후기작성)
                .map(myProductConverter::toReviewedDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MyProductDto.CompletedDto> getCompletedMyProduct(String accessToken) {
        Long userId = jwtManager.validateJwt(accessToken);

        return feedbackRepository.findByUserId(userId).stream()
                .filter(feedback -> feedback.getInvestment().getStatus() == InvestmentStatus.종료)
                .map(myProductConverter::toCompletedDto)
                .collect(Collectors.toList());
    }
}
