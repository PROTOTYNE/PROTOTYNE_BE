package com.prototyne.Enterprise.service.UserlistService;

import com.prototyne.Enterprise.converter.UserlistConverter;
import com.prototyne.Enterprise.web.dto.UserlistDTO;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.Enterprise;
import com.prototyne.domain.Investment;
import com.prototyne.domain.User;
import com.prototyne.repository.EnterpriseRepository;
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

    @Override
    public List<UserlistDTO.UserListResponse>getUserList(String token, Long eventId)
    {
        Long enterpriseId = jwtManager.validateJwt(token);
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(()->new TempHandler(ErrorStatus.LOGIN_ERROR_ID));
        List<Investment> investments= investmentRepository.findByEventId(eventId);

        return investments.stream()
                .map(investment -> UserlistConverter.toUserlistResponse(investment, investment.getUser()))
                .collect(Collectors.toList());

    }
}
