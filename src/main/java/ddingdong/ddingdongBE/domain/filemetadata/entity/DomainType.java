package ddingdong.ddingdongBE.domain.filemetadata.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DomainType {
    CLUB_PROFILE,
    CLUB_INTRODUCTION,
    FIX_ZONE_IMAGE,
    NOTICE_IMAGE,
    NOTICE_FILE,
    DOCUMENT_FILE,
    ACTIVITY_REPORT_IMAGE,
    BANNER_WEB_IMAGE,
    BANNER_MOBILE_IMAGE,
    FEED_IMAGE,
    FEED_VIDEO,
    FORM_ANSWER_FILE
}
