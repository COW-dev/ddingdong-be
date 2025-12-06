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
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.vodprocessing.repository.VodProcessingJobRepository;
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

}
