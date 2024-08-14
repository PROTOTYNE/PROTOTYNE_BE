package com.prototyne.service.MyProductService;

import com.prototyne.web.dto.MyProductDto;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

public interface MyProductService {
    List<MyProductDto> getAllMyProduct(String accessToken) throws UserPrincipalNotFoundException;
}
