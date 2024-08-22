package ddingdong.ddingdongBE.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    ILLEGAL_CLUB_LOCATION_PATTERN("올바르지 않은 동아리 위치 양식입니다."),
    ILLEGAL_CLUB_PHONE_NUMBER_PATTERN("올바르지 않은 동아리 전화번호 양식입니다."),
    ILLEGAL_PASSWORD_PATTERN("올바르지 않은 비밀번호 양식입니다."),
    ILLEGAL_SCORE_CATEGORY("올바르지 않은 점수변동내역 카테고리입니다."),
    NO_SUCH_CLUB("해당 동아리가 존재하지 않습니다."),
    NO_SUCH_NOTICE("해당 공지사항이 존재하지 않습니다."),
    NO_SUCH_ACTIVITY_REPORT("해당 활동보고서가 존재하지 않습니다."),
    NO_SUCH_QR_STAMP_HISTORY("이벤트 참여 내역이 존재하지 않습니다."),
    INVALID_CLUB_SCORE_VALUE("동아리 점수는 0 ~ 999점 입니다."),
    INVALID_STAMP_COUNT_FOR_APPLY("스탬프를 모두 모아야 이벤트에 참여할 수 있어요!"),
    ACCESS_DENIED("접근권한이 없습니다."),
    NON_VALIDATED_TOKEN("유효하지 않은 토큰입니다."),
    NO_SUCH_BANNER("해당 배너가 존재하지 않습니다."),
    NO_SUCH_FIX("해당 수리 신청서가 존재하지 않습니다."),
    NO_SUCH_FIX_ZONE_COMMENT("존재하지 않는 픽스존 댓글입니다."),
    NO_SUCH_DOCUMENT("해당 자료가 존재하지 않습니다."),
    NO_SUCH_QUESTION("해당 질문이 존재하지 않습니다.");

    private final String text;
}
