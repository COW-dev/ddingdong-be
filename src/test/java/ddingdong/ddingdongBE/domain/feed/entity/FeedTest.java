package ddingdong.ddingdongBE.domain.feed.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FeedTest {

    @DisplayName("incrementViewCount를 호출하면 viewCount가 1 증가한다.")
    @Test
    void incrementViewCount() {
        // given
        Feed feed = Feed.builder()
                .activityContent("활동 내용")
                .feedType(FeedType.IMAGE)
                .viewCount(0L)
                .build();

        // when
        feed.incrementViewCount();

        // then
        assertThat(feed.getViewCount()).isEqualTo(1L);
    }

    @DisplayName("incrementViewCount를 여러 번 호출하면 viewCount가 누적 증가한다.")
    @Test
    void incrementViewCount_multiple() {
        // given
        Feed feed = Feed.builder()
                .activityContent("활동 내용")
                .feedType(FeedType.IMAGE)
                .viewCount(0L)
                .build();

        // when
        feed.incrementViewCount();
        feed.incrementViewCount();
        feed.incrementViewCount();

        // then
        assertThat(feed.getViewCount()).isEqualTo(3L);
    }

    @DisplayName("viewCount 기본값은 0이다.")
    @Test
    void defaultViewCount() {
        // given
        Feed feed = Feed.builder()
                .activityContent("활동 내용")
                .feedType(FeedType.IMAGE)
                .build();

        // when & then
        assertThat(feed.getViewCount()).isEqualTo(0L);
    }
}
