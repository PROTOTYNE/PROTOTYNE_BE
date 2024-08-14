package com.prototyne.service.MyProductService;

import com.prototyne.converter.MyProductConverter;
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
    public List<MyProductDto> getAllMyProduct(String accessToken) throws UserPrincipalNotFoundException {
        Long userId = jwtManager.validateJwt(accessToken);

        return myProductRepository.findByUserId(userId).stream()
                .map(myProductConverter::toResponseDto)
                .collect(Collectors.toList());

//        return myProductRepository.findByUserId(userId).stream()
//                .map(investment -> MyProductDto.builder()
//                        .name(investment.getEvent().getProduct().getName())
//                        .thumbnailUrl(investment.getEvent().getProduct().getThumbnailUrl())
//                        .status(investment.getStatus())
//                        .build())
//                .collect(Collectors.toList());
    }
}
