package com.prototyne.Users.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class UserDto {
    @Builder
    @Getter
    public static class UserRequest {
        private String username;
        private String email;
        private String profileUrl;
        private Integer tickets;
        private Gender gender;
        private LocalDate birth;
    }

    @Getter
    public static class UserDetailRequest {
        private final Integer familyMember;
        private final Gender Gender;
        private final LocalDate Birth;

        public UserDetailRequest(Integer familyMember, Gender gender, LocalDate birth) {
            this.familyMember = familyMember;
            this.Gender = gender;
            this.Birth = birth;
        }
    }

    @Getter
    public static class UserAddInfoRequest {
        private final String occupation;
        private final Integer income;
        private final List<String> interests;
        private final String familyComposition;
        private final List<String> productTypes;
        private final List<String> phones;
        private final Integer healthStatus;

        public UserAddInfoRequest(String occupation, Integer income, List<String> interests, String familyComposition,
                                     List<String> productTypes, List<String> phones, Integer healthStatus){
            this.occupation = occupation;
            this.income = income;
            this.interests = interests;
            this.familyComposition = familyComposition;
            this.productTypes = productTypes;
            this.phones = phones;
            this.healthStatus = healthStatus;
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
        @JsonProperty("refresh_token")
        public String refreshToken;
        //        @JsonProperty("refresh_token_expires_in")
        //        public Integer refreshTokenExpiresIn;
        @JsonProperty("scope")
        public String scope;
        public Boolean signupComplete;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoLogoutTokenResponse{
        @JsonProperty("refresh_token")
        public String refreshToken;
        @JsonProperty("user_id")
        public Long userId;

        public KakaoLogoutTokenResponse(String refreshToken, Long userId) {
        }
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

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserSignUpRequest {
        private UserDto.UserDetailRequest detailRequest;
        private UserDto.UserAddInfoRequest addInfoRequest;
    }

    @Getter
    @AllArgsConstructor
    public static class UserSignUpResponse {
        private final Long userId;
        private final String msg;
    }

    @Data
    @Builder
    public static class UserDetailResponse {
        private final String username;
        private DetailInfo detailInfo;
        private AddInfo addInfo;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class DetailInfo {
        private int familyMember;
        private String gender;
        private String birth;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddInfo {
        private String occupation;
        private Integer income;
        private List<String> interests;
        private String familyComposition;
        private List<String> productTypes;
        private List<String> phones;
        private Integer healthStatus;

        private static final List<String> ALLOWED_OCCUPATION = Arrays.asList("STUDENT", "OFFICE", "PROFESSIONAL", "SELFEMPLOYED", "OTHER");
        private static final List<Integer> ALLOWED_INCOMES = Arrays.asList(2000, 4000, 6000, 8000, 9999);
        private static final List<String> ALLOWED_INTERESTS = Arrays.asList("FITNESS", "TRAVEL", "READING&MOVIES", "COOKING", "GAMES");
        private static final List<String> ALLOWED_FAMILY_COMPOSITIONS = Arrays.asList("1", "COUPLE", "COUPLE&CHILDREN", "PARENTS&CHILDREN", "EXTENDFAMILY");
        private static final List<String> ALLOWED_PRODUCT_TYPES = Arrays.asList("ELECTRONIC", "FASHION&BEAUTY", "FOOD", "HOUSEHOLD", "HEALTH");
        private static List<String> ALLOWED_PHONES = Arrays.asList("SMARTPHONE1", "SMARTPHONE2", "SMARTPHONE9", "TABLET", "SMARTWATCH");

        public void setOccupation(String occupation) {
            if (ALLOWED_OCCUPATION.contains(occupation)) {
                this.occupation = occupation;
            } else {
                throw new TempHandler(ErrorStatus.OCCUPATION_FORMAT_ERROR);
            }
        }

        public void setIncome(Integer income) {
            if (ALLOWED_INCOMES.contains(income)) {
                this.income = income;
            } else {
                throw new TempHandler(ErrorStatus.INCOME_FORMAT_ERROR);
            }
        }

        public void setInterests(List<String> interests) {
            validateList(interests, ALLOWED_INTERESTS, ErrorStatus.INTEREST_FORMAT_ERROR);
            this.interests = interests;
        }

        public void setFamilyComposition(String familyComposition) {
            if (!ALLOWED_FAMILY_COMPOSITIONS.contains(familyComposition)) {
                throw new TempHandler(ErrorStatus.FAMILY_FORMAT_ERROR);
            }
            this.familyComposition = familyComposition;
        }

        public void setProductTypes(List<String> productTypes) {
            validateList(productTypes, ALLOWED_PRODUCT_TYPES, ErrorStatus.PRODUCTTYPES_FORMAT_ERROR);
            this.productTypes = productTypes;
        }

        public void setPhones(List<String> phones) {
            validateList(phones, ALLOWED_PHONES, ErrorStatus.PHONES_FORMAT_ERROR);
            this.phones = phones;
        }

        public void setHealthStatus(Integer healthStatus) {
            if (healthStatus < 1 || healthStatus > 5) {
                throw new TempHandler(ErrorStatus.INVALID_HEALTHSTATUS);
            }
            this.healthStatus = healthStatus;
        }

        private void validateList(List<String> items, List<String> allowedItems, ErrorStatus errorStatus) {
            if (!new HashSet<>(allowedItems).containsAll(items)) {
                throw new TempHandler(errorStatus);
            }
        }
    }


}