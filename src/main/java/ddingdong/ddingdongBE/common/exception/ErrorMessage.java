package ddingdong.ddingdongBE.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    INTERNAL_SERVER_ERROR("서버에 문제가 발생했습니다."),
    ILLEGAL_CLUB_LOCATION_PATTERN("올바르지 않은 동아리 위치 양식입니다."),
    ILLEGAL_CLUB_PHONE_NUMBER_PATTERN("올바르지 않은 동아리 전화번호 양식입니다."),
    ILLEGAL_PASSWORD_PATTERN("올바르지 않은 비밀번호 양식입니다."),
    ILLEGAL_SCORE_CATEGORY("올바르지 않은 카테고리 양식입니다."),
    ALREADY_EXIST_CLUB_ID("이미 존재하는 동아리 계정입니다."),
    NO_SUCH_CLUB("해당 동아리가 존재하지 않습니다."),
    NO_SUCH_NOTICE("해당 공지사항이 존재하지 않습니다."),
    INVALID_CLUB_SCORE_VALUE("동아리 점수는 0 ~ 999점 입니다."),
    AUTHENTICATION_FAILURE("인증에 실패했습니다."),
    ACCESS_DENIED("접근권한이 없습니다."),
    UNREGISTER_USER("등록되지 않은 유저입니다."),
    NO_SUCH_BANNER("해당 배너가 존재하지 않습니다."),
    NON_VALIDATED_TOKEN("유효하지 않은 토큰입니다."),
    NO_SUCH_FIX("해당 수리 신청서가 존재하지 않습니다.");

    private final String text;
}
