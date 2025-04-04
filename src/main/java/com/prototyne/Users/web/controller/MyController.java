package com.prototyne.Users.web.controller;

import com.prototyne.Users.service.TicketService.TicketService;
import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.config.JwtManager;
import com.prototyne.Users.service.UserService.UserDetailServiceImpl;
import com.prototyne.Users.web.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/my")
@Tag(name = "${swagger.tag.my}")
public class MyController {

    private final TicketService ticketService;
    private final UserDetailServiceImpl userDetailService;
    private final JwtManager jwtManager;

    @GetMapping("/detail")
    @Operation(summary = "유저 정보 조회 API - 인증 필요",
            description = "유저 정보 조회 API - 인증 필요",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<UserDto.UserDetailResponse> getUserDetail(HttpServletRequest request) throws Exception {
        String accessToken = jwtManager.getToken(request);
        UserDto.UserDetailResponse userDetailResponse = userDetailService.getUserDetail(accessToken);
        return ApiResponse.onSuccess(userDetailResponse);
    }

    @PatchMapping("/basicinfo")
    @Operation(summary = "필수 정보 수정 API - 인증 필요",
            description = """
                    유저의 필수 정보를 수정합니다.
                   \s
                    [body] \s
                    *familymember: 가족 구성원 수(int), \s
                    *gender: "MALE" | "FEMALE", \s
                    *birth: "YYYY-MM-DD"
                   \s""",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<UserDto.DetailInfo> updateDetailInfo(HttpServletRequest request, @RequestBody @Valid UserDto.DetailInfo detailInfo) throws UserPrincipalNotFoundException {
        String accessToken = jwtManager.getToken(request);
        return ApiResponse.onSuccess(userDetailService.updateBasicInfo(accessToken, detailInfo));

    }

    @PatchMapping("/addinfo")
    @Operation(summary = "추가 정보 수정 API - 인증 필요",
            description = """
                     유저의 추가 정보를 수정합니다. - 수정 필요한 key-value 만 입력
                     \s
                     [body] \s
                     occupation: 직업(string) - | "STUDENT" | "OFFICE" | "PROFESSIONAL" | "SELFEMPLOYED" | "OTHER", \s
                     income: 수입(Integer) - | 2000 | 4000 | 6000 | 8000 | 9999, \s
                     interests: 관심사(string[]) -  | "FITNESS" | "TRAVEL" | "READING&MOVIES"  | "COOKING"  | "GAMES", \s
                     familyComposition: 가족 구성(string), - | 1 | "COUPLE" | "COUPLE&CHILDREN" | "PARENTS&CHILDREN" | "EXTENDFAMILY" \s
                     productTypes: 관심 상품(string[]) - | "ELECTRONIC" | "FASHION&BEAUTY" | "FOOD" | "HOUSEHOLD" | "HEALTH", \s
                     phones: 휴대폰 종류(string[]) - | "SMARTPHONE1" | "SMARTPHONE2" | "SMARTPHONE9" | "TABLET" | "SMARTWATCH", \s
                     healthStatus: 건강 상태(Integer) - | 1 | 2 | 3 | 4 | 5
                     \s
                    \s""",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<UserDto.AddInfo> updateAddInfo(HttpServletRequest request, @RequestBody @Valid UserDto.AddInfo addInfo) throws UserPrincipalNotFoundException {
        String accessToken = jwtManager.getToken(request);
        return ApiResponse.onSuccess(userDetailService.updateAddInfo(accessToken, addInfo));
    }

    //
    @GetMapping("/speed")
    @Operation(summary = "[마이페이지] 유저 스피드 조회 API - 인증 필요",
            description = "마이페이지 상단부 유저 스피드 점수 및 사용한 티켓 수 및 체험 수 조회",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<UserDto.MyPageInfo> getMyPageInfo(HttpServletRequest request) throws Exception {
        String accessToken = jwtManager.getToken(request);
        UserDto.MyPageInfo MyPageInfoResponse = userDetailService.getMyPageInfo(accessToken);
        return ApiResponse.onSuccess(MyPageInfoResponse);
    }
}
