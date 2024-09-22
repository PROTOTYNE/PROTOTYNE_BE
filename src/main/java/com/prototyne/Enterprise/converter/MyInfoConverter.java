package com.prototyne.Enterprise.converter;

import com.prototyne.Enterprise.web.dto.MyInfoDTO;
import com.prototyne.domain.Enterprise;

public class MyInfoConverter {
    public static MyInfoDTO toMyInfoResponseDTO(Enterprise enterprise){
        return MyInfoDTO.builder()
                .id(enterprise.getId())
                .name(enterprise.getName())
                .regNumber(enterprise.getRegNumber())
                .phone(enterprise.getPhone())
                .email(enterprise.getEmail())
                .address(enterprise.getAddress())
                .category(enterprise.getCategory())
                .size(enterprise.getSize())
                .build();
    }
}
