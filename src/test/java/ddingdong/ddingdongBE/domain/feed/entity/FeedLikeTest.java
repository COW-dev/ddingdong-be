package ddingdong.ddingdongBE.domain.feed.entity;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FeedLikeTest {

    @DisplayName("FeedLike를 builder로 생성하면 feed와 uuid가 정상적으로 설정된다.")
    @Test
    void create() {
        // given
        Feed feed = Feed.builder()
                .activityContent("활동 내용")
                .feedType(FeedType.IMAGE)
                .build();
        String uuid = "550e8400-e29b-41d4-a716-446655440000";

        // when
        FeedLike feedLike = FeedFixture.createFeedLike(feed, uuid);

        // then
        assertThat(feedLike.getFeed()).isEqualTo(feed);
        assertThat(feedLike.getUuid()).isEqualTo(uuid);
    }
}
