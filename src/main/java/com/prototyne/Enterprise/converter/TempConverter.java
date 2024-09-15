package com.prototyne.Enterprise.converter;

import com.prototyne.Users.web.dto.TempResponse;
import org.springframework.stereotype.Component;

@Component("enterpriseConverter")
public class TempConverter {
    public static TempResponse.TempTestDTO toTempTestDTO() {
        return TempResponse.TempTestDTO.builder()
                .testString("I am Healthy!")
                .build();
    }

    public static TempResponse.TempExceptionDTO toTempExceptionDTO(Integer flag) {
        return TempResponse.TempExceptionDTO.builder()
                .flag(flag)
                .build();
    }
}
