package com.prototyne.service.MyProductService;

import com.prototyne.web.dto.MyProductDto;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

public interface MyProductService {
    List<MyProductDto.CommonDto> getAllMyProduct(String accessToken);
    List<MyProductDto.AppliedDto> getAppliedMyProduct(String accessToken);
    List<MyProductDto.OngoingDto> getOngoingMyProduct(String accessToken);
}
