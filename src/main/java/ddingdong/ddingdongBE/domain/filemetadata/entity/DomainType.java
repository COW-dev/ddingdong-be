package ddingdong.ddingdongBE.domain.filemetadata.entity;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DomainType {
    CLUB_PROFILE(Club.class),
    CLUB_INTRODUCTION(Club.class),
    FIX_ZONE_IMAGE(FixZone.class),
    NOTICE_IMAGE(Notice.class),
    NOTICE_FILE(Notice.class),
    DOCUMENT_FILE(Document.class),
    ACTIVITY_REPORT_IMAGE(ActivityReport.class),
    BANNER_WEB_IMAGE(Banner.class),
    BANNER_MOBILE_IMAGE(Banner.class),
    FEED_IMAGE(Feed.class),
    FEED_VIDEO(Feed.class);

    private final Class<?> classType;

    public static List<DomainType> findAllByClassType(Class<?> classType) {
        return Arrays.stream(values())
            .filter(domainType -> domainType.getClassType() == classType)
            .toList();
    }
}
