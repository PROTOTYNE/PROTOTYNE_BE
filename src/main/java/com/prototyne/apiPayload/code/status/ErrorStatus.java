package com.prototyne.apiPayload.code.status;

import com.prototyne.apiPayload.code.BaseErrorCode;
import com.prototyne.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    LOGIN_ERROR_ID(HttpStatus.BAD_REQUEST, "LOGIN4001", "존재하지 않는 유저입니다"),
    TOKEN_UNVALID(HttpStatus.UNAUTHORIZED, "TOKEN4001", "유효하지 않은 토큰입니다"),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "TOKEN4002", "인증이 필요한 요청입니다"),

    // 이미 회원가입을 완료한 사용자가 다시 회원가입을 시도했을 시
    SIGNUP_DUPLICATE(HttpStatus.BAD_REQUEST, "LOGIN4001", "이미 회원가입을 완료한 유저입니다"),
    // 회원가입 시도 시에는 이미 카카오 로그인을 한 상태여야 하는데, 해당 상태를 만족하지 못했을 시
    SIGNUP_LOGIN_ERROR(HttpStatus.BAD_REQUEST, "LOGIN4001", "회원가입 시에는 먼저 카카오 로그인을 진행해야 합니다"),

    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

    // 로그인하지 않은 사용자가 북마크 여부 변경시
    HEART_ERROR_ID(HttpStatus.BAD_REQUEST, "HEART4001", "로그인이 필요합니다"),
    // 존재하지 않는 이벤트의 북마크 여부 변경시
    HEART_ERROR_EVENT(HttpStatus.BAD_REQUEST, "HEART4002", "잘못된 이벤트 접근입니다"),

    DATE_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "FORMAT4001", "잘못된 날짜 형식입니다. 올바른 형식: yyyy-MM-dd"),

    // 시제품 목록 조회 실패
    PRODUCT_ERROR_TYPE(HttpStatus.BAD_REQUEST, "PRODUCT4001", "잘못된 정렬 타입입니다."),
    // 시제품 카테고리 조회 실패
    PRODUCT_ERROR_CATEGORY(HttpStatus.BAD_REQUEST, "PRODUCT4002", "존재하지 않는 카테고리입니다."),
    // 시제품 상세보기 조회 실패
    PRODUCT_ERROR_EVENT(HttpStatus.BAD_REQUEST, "PRODUCT4003", "존재하지 않는 이벤트입니다."),

    PRODUCT_ERROR_ID(HttpStatus.BAD_REQUEST, "PRODUCT4004", "존재하지 않는 시제품입니다."),

    INVESTMENT_ERROR_ID(HttpStatus.BAD_REQUEST, "INVESTMENT4001", "존재하지 않는 나의 시제품입니다."),

    FEEDBACK_ERROR_ID(HttpStatus.BAD_REQUEST, "FEEDBACK4001", "존재하지 않는 체험 후기입니다."),

    DELIVERY_ERROR_NAME(HttpStatus.BAD_REQUEST, "DELIVERY4001", "배송지가 존재하지 않습니다."),

    INVALID_IMAGE_COUNT(HttpStatus.BAD_REQUEST, "FEEDBACK IMAGE4001", "이미지 개수의 범위를 벗어났습니다."),

    TiCKET_LACK_ERROR(HttpStatus.BAD_REQUEST, "TiCKET4001", "티켓이 부족합니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
//                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
//                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}