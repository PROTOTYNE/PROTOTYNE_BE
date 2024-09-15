package com.prototyne.Users.service.MyProductService;

import com.prototyne.Users.web.dto.MyProductDto;

import java.util.List;

public interface MyProductService {
    List<MyProductDto.CommonDto> getAllMyProduct(String accessToken);
    List<MyProductDto.AppliedDto> getAppliedMyProduct(String accessToken);
    List<MyProductDto.OngoingDto> getOngoingMyProduct(String accessToken);
    List<MyProductDto.ReviewedDto> getReviewedMyProduct(String accessToken);

    List<MyProductDto.CompletedDto> getCompletedMyProduct(String accessToken);
}
