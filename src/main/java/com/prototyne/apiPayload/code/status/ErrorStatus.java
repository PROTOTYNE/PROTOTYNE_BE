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
    LOGIN_ERROR_PW(HttpStatus.BAD_REQUEST, "LOGIN4002", "올바르지 않은 비밀번호입니다."),
    TOKEN_UNVALID(HttpStatus.UNAUTHORIZED, "TOKEN4001", "유효하지 않은 토큰입니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN4002", "토큰이 만료되었습니다"),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "TOKEN4003", "인증이 필요한 요청입니다"),
    TOKEN_UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "TOKEN500", "토큰이 존재하지 않습니다."),
    TOKEN_WRONG_TYPE_ERROR(HttpStatus.BAD_REQUEST, "TOKEN4006", "변조된 토큰입니다."),
    TOKEN_UNSUPPORTED_ERROR(HttpStatus.BAD_REQUEST, "TOKEN4007", "변조된 토큰입니다."),
    JSON_PARSING_ERROR(HttpStatus.BAD_REQUEST, "JSON4001", "JSON 파싱이 잘못되었습니다."),

    // 이미 회원가입을 완료한 사용자가 다시 회원가입을 시도했을 시
    SIGNUP_DUPLICATE(HttpStatus.BAD_REQUEST, "LOGIN4001", "이미 회원가입을 완료한 유저입니다"),
    // 회원가입 시도 시에는 이미 카카오 로그인을 한 상태여야 하는데, 해당 상태를 만족하지 못했을 시
    SIGNUP_LOGIN_ERROR(HttpStatus.BAD_REQUEST, "LOGIN4001", "회원가입 시에는 먼저 카카오 로그인을 진행해야 합니다"),

    FAMILY_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "ADDINFO4001", "잘못된 familyComposition 형식입니다."),
    PRODUCTTYPES_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "ADDINFO4001", "잘못된 productType 형식입니다."),
    INVALID_HEALTHSTATUS(HttpStatus.BAD_REQUEST, "ADDINFO4001", "잘못된 healthStatus 형식입니다."),
    INCOME_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "ADDINFO4001", "잘못된 income 형식입니다."),
    INTEREST_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "ADDINFO4001","잘못된 interests 형식입니다." ),
    OCCUPATION_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "ADDINFO4001", "잘못된 occupation 형식입니다."),
    PHONES_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "ADDINFO4001", "잘못된 phones 형식입니다."),

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

    // 시제품 삭제 시, 시제품에 연결된 eventList가 비어있지 않은 경우
    PRODUCT_ERROR_EVENTLIST(HttpStatus.BAD_REQUEST, "PRODUCT4005", "시제품에 대한 체험이 존재합니다."),

    INVESTMENT_ERROR_ID(HttpStatus.BAD_REQUEST, "INVESTMENT4001", "존재하지 않는 나의 시제품입니다."),

    FEEDBACK_ERROR_ID(HttpStatus.BAD_REQUEST, "FEEDBACK4001", "존재하지 않는 체험 후기입니다."),

    DELIVERY_ERROR_NAME(HttpStatus.BAD_REQUEST, "DELIVERY4001", "배송지가 존재하지 않습니다."),

    INVALID_IMAGE_COUNT(HttpStatus.BAD_REQUEST, "FEEDBACK IMAGE4001", "이미지 개수의 범위를 벗어났습니다."),

    TiCKET_LACK_ERROR(HttpStatus.BAD_REQUEST, "TiCKET4001", "티켓이 부족합니다."),

    USER_SPEED__LACK_ERROR(HttpStatus.BAD_REQUEST, "SPEED4001", "시속이 부족하여 신청이 불가합니다."),

    EVENT_USER_EXIST(HttpStatus.BAD_REQUEST,"EVENT4001","이미 체험 신청한 시제품입니다."),

    // 이벤트의 날짜가 null로 설정된 경우
    EVENT_ERROR_DATE(HttpStatus.BAD_REQUEST,"EVENT4002","이벤트의 날짜가 null 입니다."),

    // 이벤트가 존재하지 않은 경우
    EVENT_ERROR_ID(HttpStatus.BAD_REQUEST,"EVENT4003","존재하지 않은 이벤트 입니다."),

    // 이벤트에 유저가 투자했을 경우
    EVENT_ERROR_INVESTMENT(HttpStatus.BAD_REQUEST,"EVENT4004","유저가 투자하고 있는 체험입니다."),

    // 존재하지 않은 기업일 경우
    ENTERPRISE_ERROR_ID(HttpStatus.BAD_REQUEST, "ENTERPRISE4001", "존재하지 않는 기업입니다."),

    // 기업이 가지지 않은 시제품을 조회했을 경우
    ENTERPRISE_ERROR_PRODUCT(HttpStatus.BAD_REQUEST, "ENTERPRISE4002", "다른 기업의 시제품입니다."),

    // 업로드할 이미지 개수가 초과될 경우
    IMAGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "IMAGE4001", "업로드할 이미지 개수 초과"),

    // 기업이 대기중인 상태에서 로그인할 경우
    ENTERPRISE_ERROR_STATUS(HttpStatus.BAD_REQUEST, "ENTERPRISE4003", "승인 대기중인 기업입니다."),

    PAYMENT_NO_USER_FOUND(HttpStatus.BAD_REQUEST, "PAYMENT4001", "존재하지 않는 사용자의 결제 요청입니다."),
    PAYMENT_NO_ORDER_FOUND(HttpStatus.BAD_REQUEST, "PAYMENT4002", "유효하지 않은 결제 내역입니다."),
    PAYMENT_READY_CLIENT_FAILURE(HttpStatus.BAD_REQUEST, "PATYMENT4003", "결제 요청에 실패하였습니다."),
    PAYMENT_READY_SERVER_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "PAYMENT5001", "서버 에러, 결제 요청에 실패하였습니다."),
    PAYMENT_APPROVE_FAILURE(HttpStatus.BAD_REQUEST, "PAYMENT4004", "결제 진행 중 실패하였습니다."),
    PAYMENT_APPROVE_CANCEL(HttpStatus.BAD_REQUEST, "PAYMENT4005", "결제 진행 중 취소되었습니다."),
    PAYMENT_INVALID_PGTOKEN(HttpStatus.BAD_REQUEST, "PAYMENT4006", "유효하지 않은 pg 토큰입니다."),

    DELIVERY_LIST_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "DELIVERY4001", "배송지는 10개를 초과하여 등록할 수 없습니다."),
    DELIVERY_NOT_FOUND(HttpStatus.BAD_REQUEST, "DELIVERY4002", "존재하지 않는 배송지입니다.");

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
