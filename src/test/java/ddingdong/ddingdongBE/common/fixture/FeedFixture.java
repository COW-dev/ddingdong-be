package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedComment;
import ddingdong.ddingdongBE.domain.feed.entity.FeedType;

public class FeedFixture {

    public static Feed createImageFeed(Club club, String content) {
        return Feed.builder()
                .club(club)
                .feedType(FeedType.IMAGE)
                .activityContent(content)
                .build();
    }

    public static Feed createVideoFeed(Club club, String content) {
        return Feed.builder()
                .club(club)
                .feedType(FeedType.VIDEO)
                .activityContent(content)
                .build();
    }

    public static Feed createFeed(Club club, FeedType feedType, String content) {
        return Feed.builder()
                .club(club)
                .feedType(feedType)
                .activityContent(content)
                .build();
    }

    public static FeedComment createFeedComment(Feed feed, String uuid, int anonymousNumber, String content) {
        return FeedComment.builder()
                .feed(feed)
                .uuid(uuid)
                .anonymousNumber(anonymousNumber)
                .content(content)
                .build();
    }
}
