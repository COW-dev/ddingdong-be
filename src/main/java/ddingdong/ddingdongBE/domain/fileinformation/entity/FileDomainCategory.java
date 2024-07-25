package ddingdong.ddingdongBE.domain.fileinformation.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileDomainCategory {

    CLUB_PROFILE("club/profile/"),
    CLUB_INTRODUCE("club/introduce/"),
    NOTICE("notice/"),
    BANNER("banner/"),
    ACTIVITY_REPORT("activity-report/"),
    FIX_ZONE("fix/"),
    EVENT("event/"),
    DOCUMENT("document/");

    private final String fileDomain;
}
