package com.prototyne.Enterprise.web.dto;

import com.prototyne.domain.enums.EnterpriseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginDto {

    @Builder
    @Getter
    public static class EnterpriseSignupRequest {
        private String username;
        private String password;
        private String name;
        private String regNumber;
        private String phone;
        private String email;
        private String address;
        private String category;
        private String size;
        private EnterpriseStatus status;
    }

    @Builder
    @Getter
    public static class EnterpriseLoginRequest {
        private String username;
        private String password;
    }

    @Builder
    @Getter
    public static class EnterpriseSignupResponse {
        private Long enterpriseId;
        private String msg;
    }

    @Builder
    @Getter
    public static class EnterpriseLoginResponse {
        private Long id;
        private String name;
        private String access_token;
    }
}
