package ddingdong.ddingdongBE.domain.fileinformation.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileDomainCategory {

    CLUB("club/"),
    NOTICE("notice/"),
    BANNER("banner/"),
    ACTIVITY_REPORT("activity-report/");

    private final String fileDomain;
}