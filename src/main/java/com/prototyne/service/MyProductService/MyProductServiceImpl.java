package com.prototyne.service.MyProductService;

import com.prototyne.converter.MyProductConverter;
import com.prototyne.domain.enums.InvestmentStatus;
import com.prototyne.repository.MyProductRepository;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.web.dto.MyProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyProductServiceImpl implements MyProductService {
    private final MyProductRepository myProductRepository;
    private final JwtManager jwtManager;
    private final MyProductConverter myProductConverter;

    @Override
    public List<MyProductDto.CommonDto> getAllMyProduct(String accessToken) {
        Long userId = jwtManager.validateJwt(accessToken);

        return myProductRepository.findByUserId(userId).stream()
                .map(myProductConverter::toCommonDto)
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
}
