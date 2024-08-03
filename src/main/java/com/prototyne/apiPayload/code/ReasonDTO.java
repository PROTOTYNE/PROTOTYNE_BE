package com.prototyne.apiPayload.code;

import lombok.Getter;

@Getter
public class ReasonDTO {

    private final String code;
    private final String message;
    private final Boolean isSuccess;

    public ReasonDTO(String code, String message, Boolean isSuccess) {
        this.code = code;
        this.message = message;
        this.isSuccess = isSuccess;
    }

}