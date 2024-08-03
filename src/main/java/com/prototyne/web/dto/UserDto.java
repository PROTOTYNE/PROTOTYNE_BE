package com.prototyne.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prototyne.domain.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

public class UserDto {
    @Builder
    @Getter
    public static class UserRequest {
        private String username;
        private String email;
        private String profileUrl;
        private Integer tickets;
        private Gender gender;
        private LocalDateTime birth;
    }

    @Getter
    public static class UserDetailRequest {
        private final Integer familyMember;
        private final Gender Gender;
        private final LocalDateTime Birth;

        public UserDetailRequest(Integer familyMember, Gender gender, LocalDateTime birth) {
            this.familyMember = familyMember;
            this.Gender = gender;
            this.Birth = birth;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoTokenResponse {
        @JsonProperty("token_type")
        public String tokenType;
        @JsonProperty("access_token")
        public String accessToken;
        @JsonProperty("expires_in")
        public Integer expiresIn;
        //        @JsonProperty("refresh_token")
        //        public String refreshToken;
        //        @JsonProperty("refresh_token_expires_in")
        //        public Integer refreshTokenExpiresIn;
        @JsonProperty("scope")
        public String scope;
        public Boolean newUser;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfoResponse {
        //사용자 고유 식별값
        @JsonProperty("id")
        public Long id;

        //서비스에 연결 완료된 시각. UTC
        @JsonProperty("connected_at")
        public Date connectedAt;

        //카카오 계정 정보
        @JsonProperty("kakao_account")
        public KakaoAccount kakaoAccount;

        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class KakaoAccount {
            //사용자 프로필 정보
            @JsonProperty("profile")
            public Profile profile;

            //이메일이 인증 여부
            //true : 인증된 이메일, false : 인증되지 않은 이메일
            @JsonProperty("is_email_verified")
            public Boolean isEmailVerified;

            //카카오계정 대표 이메일
            @JsonProperty("email")
            public String email;

            @Getter
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Profile {

                //닉네임
                @JsonProperty("nickname")
                public String nickName;

                //프로필 미리보기 이미지 URL
                @JsonProperty("thumbnail_image_url")
                public String thumbnailImageUrl;

                //프로필 사진 URL
                @JsonProperty("profile_image_url")
                public String profileImageUrl;

                //프로필 사진 URL 기본 프로필인지 여부
                //true : 기본 프로필, false : 사용자 등록
                @JsonProperty("is_default_image")
                public Boolean isDefaultImage;

                //닉네임이 기본 닉네임인지 여부
                //true : 기본 닉네임, false : 사용자 등록
                @JsonProperty("is_default_nickname")
                public Boolean isDefaultNickName;
            }
        }
    }
}
