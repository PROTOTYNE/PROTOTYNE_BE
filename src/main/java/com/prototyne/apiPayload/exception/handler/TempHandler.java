package com.prototyne.apiPayload.exception.handler;


import com.prototyne.apiPayload.code.BaseErrorCode;
import com.prototyne.apiPayload.exception.GeneralException;
import lombok.Getter;

@Getter
public class TempHandler extends GeneralException {
    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}