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

    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

    // 로그인하지 않은 사용자가 북마크 여부 변경시
    HEART_ERROR_ID(HttpStatus.BAD_REQUEST, "HEART4001", "로그인이 필요합니다"),
    // 존재하지 않는 이벤트의 북마크 여부 변경시
    HEART_ERROR_EVENT(HttpStatus.BAD_REQUEST, "HEART4002", "잘못된 이벤트 접근입니다");

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