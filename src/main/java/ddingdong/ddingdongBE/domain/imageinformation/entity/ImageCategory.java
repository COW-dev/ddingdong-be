package ddingdong.ddingdongBE.domain.imageinformation.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageCategory {

    CLUB("club-image/"),
    NOTICE("notice-image/"),
    BANNER("banner-image/"),
    ACTIVITY_REPORT("activity-report-image/");

    private final String filePath;
}