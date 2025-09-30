package ddingdong.ddingdongBE.domain.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;
import ddingdong.ddingdongBE.domain.vodprocessing.repository.VodProcessingJobRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;

class FeedRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    @Autowired
    private VodProcessingJobRepository vodProcessingJobRepository;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @BeforeEach
    void setUp() {
        feedRepository.deleteAll();
        feedRepository.flush();
        clubRepository.deleteAll();
        clubRepository.flush();
    }

    @DisplayName("모든 동아리의 최신 피드 페이지를 주어진 정보에 맞춰 반환한다.")
    @Test
    void findNewestPerClubPage() {
        // given
        Club club1 = fixture.giveMeBuilder(Club.class)
                .set("id", null)
                .set("name", "카우1")
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .sample();
        Club club2 = fixture.giveMeBuilder(Club.class)
                .set("id", null)
                .set("name", "카우2")
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .sample();
        Club club3 = fixture.giveMeBuilder(Club.class)
                .set("id", null)
                .set("name", "카우3")
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .sample();
        Club savedClub1 = clubRepository.save(club1);
        Club savedClub2 = clubRepository.save(club2);
        Club savedClub3 = clubRepository.save(club3);

        Feed feed1 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub1)
                .set("activityContent", "내용 1 올드")
                .set("feedType", FeedType.IMAGE)
                .sample();
        Feed feed2 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub1)
                .set("activityContent", "내용 1 최신")
                .set("feedType", FeedType.IMAGE)
                .sample();
        Feed feed3 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub2)
                .set("activityContent", "내용 2 올드")
                .set("feedType", FeedType.IMAGE)
                .sample();
        Feed feed4 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub2)
                .set("activityContent", "내용 2 최신")
                .set("feedType", FeedType.IMAGE)
                .sample();
        Feed feed5 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub3)
                .set("activityContent", "내용 3 올드")
                .set("feedType", FeedType.IMAGE)
                .sample();
        Feed feed6 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub3)
                .set("activityContent", "내용 3 최신")
                .set("feedType", FeedType.IMAGE)
                .sample();
        feedRepository.saveAll(List.of(feed1, feed2, feed3, feed4, feed5, feed6));

        int size = 2;
        Long currentCursorId = -1L;
        // when
        Slice<Feed> newestFeeds = feedRepository.findNewestPerClubPage(size, currentCursorId);

        // then
        List<Feed> feeds = newestFeeds.getContent();
        assertThat(feeds.size()).isEqualTo(2);
        assertThat(feeds.get(0).getId()).isEqualTo(6);
        assertThat(feeds.get(1).getId()).isEqualTo(4);
    }

    @DisplayName("size 개수보다 남은 feed의 개수가 적다면, 그 수만큼 페이지로 반환한다.")
    @Test
    void 페이지네이션_남은_개수가_사이즈보다_적은경우() {
        // given
        Club club = fixture.giveMeBuilder(Club.class)
                .set("id", null)
                .set("name", "카우")
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .sample();
        Club savedClub = clubRepository.save(club);

        Feed feed1 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub)
                .set("activityContent", "내용1")
                .set("feedType", FeedType.IMAGE)
                .sample();
        Feed feed2 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub)
                .set("activityContent", "내용2")
                .set("feedType", FeedType.IMAGE)
                .sample();
        Feed feed3 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub)
                .set("activityContent", "내용3")
                .set("feedType", FeedType.IMAGE)
                .sample();
        Feed feed4 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub)
                .set("activityContent", "내용4")
                .set("feedType", FeedType.IMAGE)
                .sample();
        feedRepository.saveAll(List.of(feed1, feed2, feed3, feed4));

        Long clubId = savedClub.getId();
        int size = 2;
        Long cursorId = 2L;
        // when
        Slice<Feed> page = feedRepository.findPageByClubIdOrderById(clubId, size, cursorId);
        // then
        List<Feed> feeds = page.getContent();
        assertThat(feeds.size()).isEqualTo(1);
        assertThat(feeds.get(0).getId()).isEqualTo(1);
        assertThat(feeds.get(0).getActivityContent()).isEqualTo(feed1.getActivityContent());
    }

    @DisplayName("cursorId보다 작은 Feed를 size 개수만큼 페이지로 반환한다.")
    @Test
    void findPageByClubIdOrderById() {
        // given
        Club club = fixture.giveMeBuilder(Club.class)
                .set("id", null)
                .set("name", "카우")
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .sample();
        Club savedClub = clubRepository.save(club);

        Feed feed1 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub)
                .set("activityContent", "내용1")
                .set("feedType", FeedType.IMAGE)
                .sample();
        Feed feed2 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub)
                .set("activityContent", "내용2")
                .set("feedType", FeedType.IMAGE)
                .sample();
        Feed feed3 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub)
                .set("activityContent", "내용3")
                .set("feedType", FeedType.IMAGE)
                .sample();
        Feed feed4 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub)
                .set("activityContent", "내용4")
                .set("feedType", FeedType.IMAGE)
                .sample();
        feedRepository.saveAll(List.of(feed1, feed2, feed3, feed4));

        Long clubId = savedClub.getId();
        int size = 2;
        Long cursorId = 4L;
        // when
        Slice<Feed> page = feedRepository.findPageByClubIdOrderById(clubId, size, cursorId);
        // then
        List<Feed> feeds = page.getContent();
        assertThat(feeds.size()).isEqualTo(2);
        assertThat(feeds.get(0).getId()).isEqualTo(feed3.getId());
        assertThat(feeds.get(0).getActivityContent()).isEqualTo(feed3.getActivityContent());
        assertThat(feeds.get(1).getId()).isEqualTo(feed2.getId());
        assertThat(feeds.get(1).getActivityContent()).isEqualTo(feed2.getActivityContent());
    }

    @DisplayName("동아리 피드 목록 조회 - VIDEO 피드일 경우 vodJopProcessingJob 상태가 COMPLETE인것만 조회 ")
    @Test
    void 동아리_피드_목록_조회() {
        // given
        Club club = fixture.giveMeBuilder(Club.class)
                .set("id", null)
                .set("name", "카우")
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .sample();
        Club savedClub = clubRepository.save(club);

        Feed feed1 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub)
                .set("activityContent", "내용1")
                .set("feedType", FeedType.VIDEO)
                .sample();
        Feed feed2 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub)
                .set("activityContent", "내용2")
                .set("feedType", FeedType.VIDEO)
                .sample();
        feedRepository.saveAll(List.of(feed1, feed2));

        UUID id = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        DomainType domainType = DomainType.FEED_VIDEO;
        FileMetaData fileMetaData = FileMetaData.builder()
                .id(id)
                .fileKey("123")
                .fileName("1234.png")
                .domainType(domainType)
                .entityId(feed1.getId())
                .fileStatus(FileStatus.COUPLED)
                .build();
        FileMetaData fileMetaData2 = FileMetaData.builder()
                .id(id2)
                .fileKey("123")
                .fileName("1234.png")
                .domainType(domainType)
                .entityId(feed2.getId())
                .fileStatus(FileStatus.COUPLED)
                .build();
        fileMetaDataRepository.saveAll(List.of(fileMetaData, fileMetaData2));

        VodProcessingJob vodProcessingJob1 = fixture.giveMeBuilder(VodProcessingJob.class)
                .set("id", null)
                .set("vodProcessingNotification", null)
                .set("fileMetaData", fileMetaData)
                .set("convertJobStatus", ConvertJobStatus.COMPLETE)
                .sample();
        VodProcessingJob vodProcessingJob2 = fixture.giveMeBuilder(VodProcessingJob.class)
                .set("id", null)
                .set("vodProcessingNotification", null)
                .set("fileMetaData", fileMetaData2)
                .set("convertJobStatus", ConvertJobStatus.PENDING)
                .sample();
        vodProcessingJobRepository.saveAll(List.of(vodProcessingJob1, vodProcessingJob2));
        Long clubId = savedClub.getId();
        int size = 2;
        Long currentCursorId = -1L;
        // when
        Slice<Feed> findFeedsByClub = feedRepository.findPageByClubIdOrderById(clubId, size, currentCursorId);

        // then
        List<Feed> feeds = findFeedsByClub.getContent();
        assertThat(feeds.size()).isEqualTo(1);
        assertThat(feeds.get(0).getId()).isEqualTo(1);
    }

    @DisplayName("모든 동아리 최신 피드 조회 - VIDEO 피드일 경우 vodJopProcessingJob 상태가 COMPLETE인것만 조회 ")
    @Test
    void 모든_동아리_최신_피드_조회() {
        // given
        Club club = fixture.giveMeBuilder(Club.class)
                .set("id", null)
                .set("name", "카우")
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .sample();
        Club savedClub = clubRepository.save(club);

        Feed feed1 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub)
                .set("activityContent", "내용1")
                .set("feedType", FeedType.VIDEO)
                .sample();
        Feed feed2 = fixture.giveMeBuilder(Feed.class)
                .set("id", null)
                .set("club", savedClub)
                .set("activityContent", "내용2")
                .set("feedType", FeedType.VIDEO)
                .sample();
        feedRepository.saveAll(List.of(feed1, feed2));

        UUID id = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        DomainType domainType = DomainType.FEED_VIDEO;
        FileMetaData fileMetaData = FileMetaData.builder()
                .id(id)
                .fileKey("123")
                .fileName("1234.png")
                .domainType(domainType)
                .entityId(feed1.getId())
                .fileStatus(FileStatus.COUPLED)
                .build();
        FileMetaData fileMetaData2 = FileMetaData.builder()
                .id(id2)
                .fileKey("123")
                .fileName("1234.png")
                .domainType(domainType)
                .entityId(feed2.getId())
                .fileStatus(FileStatus.COUPLED)
                .build();
        fileMetaDataRepository.saveAll(List.of(fileMetaData, fileMetaData2));

        VodProcessingJob vodProcessingJob1 = fixture.giveMeBuilder(VodProcessingJob.class)
                .set("id", null)
                .set("vodProcessingNotification", null)
                .set("fileMetaData", fileMetaData)
                .set("convertJobStatus", ConvertJobStatus.COMPLETE)
                .sample();
        VodProcessingJob vodProcessingJob2 = fixture.giveMeBuilder(VodProcessingJob.class)
                .set("id", null)
                .set("vodProcessingNotification", null)
                .set("fileMetaData", fileMetaData2)
                .set("convertJobStatus", ConvertJobStatus.COMPLETE)
                .sample();
        vodProcessingJobRepository.saveAll(List.of(vodProcessingJob1, vodProcessingJob2));

        int size = 2;
        Long currentCursorId = -1L;
        // when
        Slice<Feed> findFeedsByClub = feedRepository.findNewestPerClubPage(size, currentCursorId);

        // then
        List<Feed> feeds = findFeedsByClub.getContent();
        assertThat(feeds.size()).isEqualTo(1);
        assertThat(feeds.get(0).getId()).isEqualTo(2);
    }
}
