package com.prototyne.Enterprise.web.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyInfoDTO {
    private Long id;

    private String name;

    private String regNumber;

    private String phone;

    private String email;

    private String address;

    private String category;

    private String size;
}
