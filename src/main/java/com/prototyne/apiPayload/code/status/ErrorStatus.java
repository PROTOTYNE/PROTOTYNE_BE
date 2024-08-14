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

    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

    DATE_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "FORMAT4001", "잘못된 날짜 형식입니다. 올바른 형식: yyyy-MM-dd"),

    // 시제품 목록 조회 실패
    PRODUCT_ERROR_TYPE(HttpStatus.BAD_REQUEST, "PRODUCT4001", "잘못된 정렬 타입입니다."),
    // 시제품 카테고리 조회 실패
    PRODUCT_ERROR_CATEGORY(HttpStatus.BAD_REQUEST, "PRODUCT4002", "존재하지 않는 카테고리입니다."),
    // 시제품 상세보기 조회 실패
    PRODUCT_ERROR_EVENT(HttpStatus.BAD_REQUEST, "PRODUCT4003", "존재하지 않는 이벤트입니다.");

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