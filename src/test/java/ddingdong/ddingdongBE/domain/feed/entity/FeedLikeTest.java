package ddingdong.ddingdongBE.domain.feed.entity;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FeedLikeTest {

    @DisplayName("FeedLike를 builder로 생성하면 feed와 user가 정상적으로 설정된다.")
    @Test
    void create() {
        // given
        Feed feed = Feed.builder()
                .activityContent("활동 내용")
                .feedType(FeedType.IMAGE)
                .build();
        User user = UserFixture.createGeneralUser();

        // when
        FeedLike feedLike = FeedFixture.createFeedLike(feed, user);

        // then
        assertThat(feedLike.getFeed()).isEqualTo(feed);
        assertThat(feedLike.getUser()).isEqualTo(user);
    }
}
