package com.prototyne.Enterprise.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class TempDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempTestDTO{
        String testString;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempExceptionDTO{
        Integer flag;
    }

    // 이미지 업로드 테스트 - url 반환
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempUploadDTO{
        private List<String> testStrings;
    }
}