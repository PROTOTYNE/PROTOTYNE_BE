package com.prototyne.Enterprise.service.UserlistService;

import com.prototyne.Enterprise.converter.UserlistConverter;
import com.prototyne.Enterprise.web.dto.UserlistDTO;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.Enterprise;
import com.prototyne.domain.Investment;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.InvestmentShipping;
import com.prototyne.domain.enums.InvestmentStatus;
import com.prototyne.repository.EnterpriseRepository;
import com.prototyne.repository.FeedbackRepository;
import com.prototyne.repository.InvestmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("enterpriseUserlistServiceImpl")
@AllArgsConstructor
public class UserlistServiceImpl implements UserlistService{
    private final JwtManager jwtManager;
    private final InvestmentRepository investmentRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final FeedbackRepository feedbackRepository;

    private String determineReviewStatus(Long investmentId, Long userId) {
        boolean isReviewWritten = feedbackRepository.existsByInvestmentIdAndUserId(investmentId, userId);
        return isReviewWritten ? "작성" : "미작성";
    }

    @Override
    public List<UserlistDTO.UserListResponse>getUserList(String token, Long eventId)
    {
        Long enterpriseId = jwtManager.validateJwt(token);
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(()->new TempHandler(ErrorStatus.LOGIN_ERROR_ID));
        List<Investment> investments= investmentRepository.findByEventId(eventId);



        return investments.stream()
                .map(investment ->
                        {
                            String reviewStatus = determineReviewStatus(investment.getId(), investment.getUser().getId());

                            return UserlistConverter.toUserlistResponse(investment, investment.getUser(),reviewStatus);
                        }
                )
                .collect(Collectors.toList());

    }

    @Override
    public UserlistDTO.UserListResponse updateUserPrize(String token, Long eventId, Long userId, Boolean isPrize){
        Long enterpriseId = jwtManager.validateJwt(token);
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(()->new TempHandler(ErrorStatus.LOGIN_ERROR_ID));
        Investment investment=investmentRepository.findFirstByUserIdAndEventId(userId, eventId)
                .orElseThrow(()->new TempHandler((ErrorStatus.PRODUCT_ERROR_EVENT)));

        if(isPrize){
            investment.setStatus(InvestmentStatus.당첨);
        }
        else{
            investment.setStatus(InvestmentStatus.신청);
        }

        investmentRepository.save(investment);

        String reviewStatus = determineReviewStatus(investment.getId(), userId);
        return UserlistConverter.toUserlistResponse(investment, investment.getUser(),reviewStatus);

    }

    @Override
    public UserlistDTO.UserListResponse updateUserDelivery(String token, Long eventId, Long userId, Boolean isDelivery){
        Long enterpriseId = jwtManager.validateJwt(token);
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(()->new TempHandler(ErrorStatus.LOGIN_ERROR_ID));
        Investment investment=investmentRepository.findFirstByUserIdAndEventId(userId, eventId)
                .orElseThrow(()->new TempHandler((ErrorStatus.PRODUCT_ERROR_EVENT)));

        if(isDelivery){

            investment.setShipping(InvestmentShipping.배달완료);
        }
        else{
            investment.setShipping(InvestmentShipping.배송전);
        }

        investmentRepository.save(investment);
        String reviewStatus = determineReviewStatus(investment.getId(), userId);
        return UserlistConverter.toUserlistResponse(investment, investment.getUser(),reviewStatus);
    }


}
