package ddingdong.ddingdongBE.domain.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.fixture.VodProcessingJobFixture;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.dto.MonthlyFeedRankingDto;
import ddingdong.ddingdongBE.domain.feed.repository.dto.MyFeedStatDto;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.vodprocessing.repository.VodProcessingJobRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;

class FeedRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    @Autowired
    private VodProcessingJobRepository vodProcessingJobRepository;

    @Autowired
    private FeedCommentRepository feedCommentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("클럽별 피드 페이지 조회 - IMAGE 타입 피드는 조회된다")
    @Test
    void findPageByClubIdOrderById_ImageFeeds() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed imageFeed1 = feedRepository.save(FeedFixture.createImageFeed(club, "이미지 피드 1"));
        Feed imageFeed2 = feedRepository.save(FeedFixture.createImageFeed(club, "이미지 피드 2"));

        // when
        Slice<Feed> result = feedRepository.findPageByClubIdOrderById(club.getId(), 10, -1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getContent()).hasSize(2);
            softly.assertThat(result.getContent()).extracting(Feed::getActivityContent)
                    .containsExactly("이미지 피드 2", "이미지 피드 1");
        });
    }

    @DisplayName("클럽별 피드 페이지 조회 - VIDEO 타입 피드 중 VOD 처리 완료된 피드만 조회된다")
    @Test
    void findPageByClubIdOrderById_VideoFeedsWithCompleteVod() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed videoFeed1 = feedRepository.save(FeedFixture.createVideoFeed(club, "비디오 피드 1"));
        Feed videoFeed2 = feedRepository.save(FeedFixture.createVideoFeed(club, "비디오 피드 2"));

        // 첫 번째 비디오만 VOD 처리 완료
        FileMetaData fileMetaData = fileMetaDataRepository.save(VodProcessingJobFixture.createFileMetaData(videoFeed1.getId()));
        vodProcessingJobRepository.save(VodProcessingJobFixture.createCompleteVodProcessingJob(fileMetaData));

        // when
        Slice<Feed> result = feedRepository.findPageByClubIdOrderById(club.getId(), 10, -1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getContent()).hasSize(1);
            softly.assertThat(result.getContent().get(0).getActivityContent()).isEqualTo("비디오 피드 1");
        });
    }

    @DisplayName("클럽별 피드 페이지 조회 - 커서 기반 페이지네이션이 동작한다")
    @Test
    void findPageByClubIdOrderById_CursorPagination() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed1 = feedRepository.save(FeedFixture.createImageFeed(club, "피드 1"));
        Feed feed2 = feedRepository.save(FeedFixture.createImageFeed(club, "피드 2"));
        Feed feed3 = feedRepository.save(FeedFixture.createImageFeed(club, "피드 3"));

        // when
        Slice<Feed> firstPage = feedRepository.findPageByClubIdOrderById(club.getId(), 2, -1L);
        Long lastId = firstPage.getContent().get(firstPage.getContent().size() - 1).getId();
        Slice<Feed> secondPage = feedRepository.findPageByClubIdOrderById(club.getId(), 2, lastId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(firstPage.getContent()).hasSize(2);
            softly.assertThat(secondPage.getContent()).hasSize(1);
            softly.assertThat(secondPage.getContent().get(0).getActivityContent()).isEqualTo("피드 1");
        });
    }

    @DisplayName("클럽별 피드 페이지 조회 - 삭제된 피드는 조회되지 않는다")
    @Test
    void findPageByClubIdOrderById_ExcludesDeletedFeeds() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed activeFeed = feedRepository.save(FeedFixture.createImageFeed(club, "활성 피드"));
        Feed deletedFeed = feedRepository.save(FeedFixture.createImageFeed(club, "삭제된 피드"));

        // 피드 삭제 (soft delete)
        feedRepository.delete(deletedFeed);
        feedRepository.flush();

        // when
        Slice<Feed> result = feedRepository.findPageByClubIdOrderById(club.getId(), 10, -1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getContent()).hasSize(1);
            softly.assertThat(result.getContent().get(0).getActivityContent()).isEqualTo("활성 피드");
        });
    }

    @DisplayName("전체 최신 피드 페이지 조회 - IMAGE 타입 피드는 조회된다")
    @Test
    void getAllFeedPage_ImageFeeds() {
        // given
        Club club1 = clubRepository.save(ClubFixture.createClub());
        Club club2 = clubRepository.save(ClubFixture.createClub());
        Feed imageFeed1 = feedRepository.save(FeedFixture.createImageFeed(club1, "이미지 피드 1"));
        Feed imageFeed2 = feedRepository.save(FeedFixture.createImageFeed(club2, "이미지 피드 2"));

        // when
        Slice<Feed> result = feedRepository.getAllFeedPage(10, -1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getContent()).hasSize(2);
            softly.assertThat(result.getContent()).extracting(Feed::getActivityContent)
                    .containsExactly("이미지 피드 2", "이미지 피드 1");
        });
    }

    @DisplayName("전체 최신 피드 페이지 조회 - VIDEO 타입 피드 중 VOD 처리 완료된 피드만 조회된다")
    @Test
    void getAllFeedPage_VideoFeedsWithCompleteVod() {
        // given
        Club club1 = clubRepository.save(ClubFixture.createClub());
        Club club2 = clubRepository.save(ClubFixture.createClub());
        Feed videoFeed1 = feedRepository.save(FeedFixture.createVideoFeed(club1, "비디오 피드 1"));
        Feed videoFeed2 = feedRepository.save(FeedFixture.createVideoFeed(club2, "비디오 피드 2"));

        // 첫 번째 비디오만 VOD 처리 완료
        FileMetaData fileMetaData = fileMetaDataRepository.save(VodProcessingJobFixture.createFileMetaData(videoFeed1.getId()));
        vodProcessingJobRepository.save(VodProcessingJobFixture.createCompleteVodProcessingJob(fileMetaData));

        // when
        Slice<Feed> result = feedRepository.getAllFeedPage(10, -1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getContent()).hasSize(1);
            softly.assertThat(result.getContent().get(0).getActivityContent()).isEqualTo("비디오 피드 1");
        });
    }

    @DisplayName("전체 최신 피드 페이지 조회 - 커서 기반 페이지네이션이 동작한다")
    @Test
    void getAllFeedPage_CursorPagination() {
        // given
        Club club1 = clubRepository.save(ClubFixture.createClub());
        Club club2 = clubRepository.save(ClubFixture.createClub());
        Club club3 = clubRepository.save(ClubFixture.createClub());
        Feed feed1 = feedRepository.save(FeedFixture.createImageFeed(club1, "피드 1"));
        Feed feed12 = feedRepository.save(FeedFixture.createImageFeed(club1, "피드 12"));
        Feed feed2 = feedRepository.save(FeedFixture.createImageFeed(club2, "피드 2"));
        Feed feed3 = feedRepository.save(FeedFixture.createImageFeed(club3, "피드 3"));

        // when
        Slice<Feed> firstPage = feedRepository.getAllFeedPage(2, -1L);
        Long lastId = firstPage.getContent().get(firstPage.getContent().size() - 1).getId();
        Slice<Feed> secondPage = feedRepository.getAllFeedPage(2, lastId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(firstPage.getContent()).hasSize(2);
            softly.assertThat(secondPage.getContent()).hasSize(2);
            softly.assertThat(secondPage.getContent().get(0).getActivityContent()).isEqualTo("피드 12");
        });
    }

    @DisplayName("전체 최신 피드 페이지 조회 - 삭제된 피드는 조회되지 않는다")
    @Test
    void getAllFeedPage_ExcludesDeletedFeeds() {
        // given
        Club club1 = clubRepository.save(ClubFixture.createClub());
        Club club2 = clubRepository.save(ClubFixture.createClub());
        Feed activeFeed = feedRepository.save(FeedFixture.createImageFeed(club1, "활성 피드"));
        Feed deletedFeed = feedRepository.save(FeedFixture.createImageFeed(club2, "삭제된 피드"));

        // 피드 삭제 (soft delete)
        feedRepository.delete(deletedFeed);
        feedRepository.flush();

        // when
        Slice<Feed> result = feedRepository.getAllFeedPage(10, -1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getContent()).hasSize(1);
            softly.assertThat(result.getContent().get(0).getActivityContent()).isEqualTo("활성 피드");
        });
    }

    @DisplayName("전체 최신 피드 페이지 조회 - 여러 클럽의 피드가 최신순으로 정렬된다")
    @Test
    void getAllFeedPage_OrderedByIdDesc() {
        // given
        Club club1 = clubRepository.save(ClubFixture.createClub());
        Club club2 = clubRepository.save(ClubFixture.createClub());

        Feed oldFeed = feedRepository.save(FeedFixture.createImageFeed(club1, "오래된 피드"));
        Feed newFeed = feedRepository.save(FeedFixture.createImageFeed(club2, "최신 피드"));

        // when
        Slice<Feed> result = feedRepository.getAllFeedPage(10, -1L);

        // then
        List<Feed> feeds = result.getContent();
        assertThat(feeds.get(0).getId()).isGreaterThan(feeds.get(1).getId());
    }

    @DisplayName("incrementViewCount로 조회수가 1 증가한다")
    @Test
    void incrementViewCount_IncreasesBy1() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "피드"));
        assertThat(feed.getViewCount()).isZero();

        // when
        feedRepository.incrementViewCount(feed.getId());
        entityManager.flush();
        entityManager.clear();

        // then
        Feed found = feedRepository.findById(feed.getId()).orElseThrow();
        assertThat(found.getViewCount()).isEqualTo(1);
    }

    @DisplayName("incrementViewCount를 여러 번 호출하면 누적된다")
    @Test
    void incrementViewCount_AccumulatesMultipleCalls() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "피드"));

        // when
        feedRepository.incrementViewCount(feed.getId());
        feedRepository.incrementViewCount(feed.getId());
        feedRepository.incrementViewCount(feed.getId());
        entityManager.flush();
        entityManager.clear();

        // then
        Feed found = feedRepository.findById(feed.getId()).orElseThrow();
        assertThat(found.getViewCount()).isEqualTo(3);
    }

    @DisplayName("findMonthlyRankingByClub으로 월별 동아리 랭킹을 조회한다")
    @Test
    void findMonthlyRankingByClub_ReturnsRankingsWithCorrectScore() {
        // given
        Club clubA = clubRepository.save(ClubFixture.createClub("동아리A"));
        Club clubB = clubRepository.save(ClubFixture.createClub("동아리B"));

        Feed feedA1 = feedRepository.save(FeedFixture.createImageFeed(clubA, "A 피드 1"));
        Feed feedA2 = feedRepository.save(FeedFixture.createImageFeed(clubA, "A 피드 2"));
        Feed feedB1 = feedRepository.save(FeedFixture.createImageFeed(clubB, "B 피드 1"));

        feedRepository.addLikeCount(feedA1.getId(), 1);
        feedCommentRepository.save(FeedFixture.createFeedComment(feedA1, "uuid-2", 1, "댓글"));

        entityManager.flush();
        entityManager.clear();

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        // when
        List<MonthlyFeedRankingDto> result = feedRepository.findMonthlyRankingByClub(year, month);

        // then
        assertThat(result).hasSize(2);

        MonthlyFeedRankingDto rankingA = result.stream()
                .filter(r -> r.getClubId().equals(clubA.getId()))
                .findFirst().orElseThrow();
        MonthlyFeedRankingDto rankingB = result.stream()
                .filter(r -> r.getClubId().equals(clubB.getId()))
                .findFirst().orElseThrow();

        assertSoftly(softly -> {
            // Club A: feedCount=2, viewCount=0, likeCount=1, commentCount=1
            softly.assertThat(rankingA.getFeedCount()).isEqualTo(2);
            softly.assertThat(rankingA.getViewCount()).isEqualTo(0);
            softly.assertThat(rankingA.getLikeCount()).isEqualTo(1);
            softly.assertThat(rankingA.getCommentCount()).isEqualTo(1);

            // Club B: feedCount=1, viewCount=0, likeCount=0, commentCount=0
            softly.assertThat(rankingB.getFeedCount()).isEqualTo(1);
            softly.assertThat(rankingB.getViewCount()).isEqualTo(0);
            softly.assertThat(rankingB.getLikeCount()).isEqualTo(0);
            softly.assertThat(rankingB.getCommentCount()).isEqualTo(0);
        });
    }

    @DisplayName("findMonthlyRankingByClub에서 삭제된 피드는 제외된다")
    @Test
    void findMonthlyRankingByClub_ExcludesDeletedFeeds() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub("동아리"));
        Feed activeFeed = feedRepository.save(FeedFixture.createImageFeed(club, "활성 피드"));
        Feed deletedFeed = feedRepository.save(FeedFixture.createImageFeed(club, "삭제 피드"));

        feedRepository.delete(deletedFeed);
        entityManager.flush();
        entityManager.clear();

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        // when
        List<MonthlyFeedRankingDto> result = feedRepository.findMonthlyRankingByClub(year, month);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFeedCount()).isEqualTo(1);
    }

    @DisplayName("동아리의 피드 수, 조회수, 이미지/비디오 수를 집계한다")
    @Test
    void findMyFeedStat_ReturnsCorrectStats() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed imageFeed1 = feedRepository.save(FeedFixture.createImageFeed(club, "이미지 1"));
        Feed imageFeed2 = feedRepository.save(FeedFixture.createImageFeed(club, "이미지 2"));
        Feed videoFeed = feedRepository.save(FeedFixture.createVideoFeed(club, "비디오 1"));

        feedRepository.incrementViewCount(imageFeed1.getId());
        feedRepository.incrementViewCount(imageFeed1.getId());
        feedRepository.incrementViewCount(videoFeed.getId());
        entityManager.flush();
        entityManager.clear();

        // when
        MyFeedStatDto stat = feedRepository.findMyFeedStat(club.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(stat.getFeedCount()).isEqualTo(3);
            softly.assertThat(stat.getTotalViewCount()).isEqualTo(3);
            softly.assertThat(stat.getImageCount()).isEqualTo(2);
            softly.assertThat(stat.getVideoCount()).isEqualTo(1);
        });
    }

    @DisplayName("삭제된 피드는 집계에서 제외된다")
    @Test
    void findMyFeedStat_ExcludesDeletedFeeds() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());
        feedRepository.save(FeedFixture.createImageFeed(club, "활성 피드"));
        Feed deletedFeed = feedRepository.save(FeedFixture.createImageFeed(club, "삭제 피드"));

        feedRepository.delete(deletedFeed);
        entityManager.flush();
        entityManager.clear();

        // when
        MyFeedStatDto stat = feedRepository.findMyFeedStat(club.getId());

        // then
        assertThat(stat.getFeedCount()).isEqualTo(1);
    }

    @DisplayName("피드가 없으면 모든 집계 값이 0이다")
    @Test
    void findMyFeedStat_ReturnsZerosWhenNoFeeds() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());

        // when
        MyFeedStatDto stat = feedRepository.findMyFeedStat(club.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(stat.getFeedCount()).isEqualTo(0);
            softly.assertThat(stat.getTotalViewCount()).isEqualTo(0);
            softly.assertThat(stat.getImageCount()).isEqualTo(0);
            softly.assertThat(stat.getVideoCount()).isEqualTo(0);
        });
    }

    @DisplayName("findMonthlyRankingByClub에서 다른 월 피드는 제외된다")
    @Test
    void findMonthlyRankingByClub_ExcludesDifferentMonthFeeds() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub("동아리"));
        feedRepository.save(FeedFixture.createImageFeed(club, "이번 달 피드"));
        Feed otherMonthFeed = feedRepository.save(FeedFixture.createImageFeed(club, "다른 달 피드"));

        entityManager.flush();

        LocalDateTime twoMonthsAgo = LocalDateTime.now().minusMonths(2);
        entityManager.createNativeQuery("UPDATE feed SET created_at = :date WHERE id = :id")
                .setParameter("date", twoMonthsAgo)
                .setParameter("id", otherMonthFeed.getId())
                .executeUpdate();
        entityManager.flush();
        entityManager.clear();

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        // when
        List<MonthlyFeedRankingDto> result = feedRepository.findMonthlyRankingByClub(year, month);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFeedCount()).isEqualTo(1);
    }

}
