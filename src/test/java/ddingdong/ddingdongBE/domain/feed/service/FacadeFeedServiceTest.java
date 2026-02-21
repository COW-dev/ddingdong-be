package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.github.f4b6a3.uuid.UuidCreator;
import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedType;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedVideoUrlQuery;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class FacadeFeedServiceTest extends TestContainerSupport {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FacadeFeedService facadeFeedService;

    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    @MockitoBean
    private S3FileService s3FileService;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @BeforeEach
    void setUp() {
        feedRepository.deleteAll();
        feedRepository.flush();
        clubRepository.deleteAll();
        clubRepository.flush();
    }

    @DisplayName("모든 사용자는 동아리 피드에 대해 상세 조회할 수 있다.")
    @Test
    void getFeedById() {
        // given
        Club club = fixture.giveMeBuilder(Club.class)
                .setNull("id")
                .set("name", "카우")
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sample();
        Club savedClub = clubRepository.save(club);
        DomainType clubDomainType = DomainType.CLUB_PROFILE;
        Long clubEntityId = 1L;
        UUID id1 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.save(
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", id1)
                        .set("domainType", clubDomainType)
                        .set("entityId", clubEntityId)
                        .set("fileStatus", FileStatus.COUPLED)
                        .sample()
        );

        LocalDateTime now = LocalDateTime.now();
        Feed feed = fixture.giveMeBuilder(Feed.class)
                .setNull("id")
                .set("club", savedClub)
                .set("activityContent", "카우 활동내역")
                .set("feedType", FeedType.IMAGE)
                .set("createdAt", now)
                .sample();
        Feed savedFeed = feedRepository.save(feed);

        DomainType domainType = DomainType.FEED_IMAGE;
        Long entityId = 1L;
        UUID id2 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.save(
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", id2)
                        .set("domainType", domainType)
                        .set("entityId", entityId)
                        .set("fileStatus", FileStatus.COUPLED)
                        .sample()
        );

        BDDMockito.given(s3FileService.getUploadedFileUrl(any()))
                .willReturn(new UploadedFileUrlQuery(null, null, null));
        BDDMockito.given(s3FileService.getUploadedFileUrlAndName(any(), any()))
                .willReturn(new UploadedFileUrlAndNameQuery(null, null, null, null));
        BDDMockito.given(s3FileService.getUploadedVideoUrl(any()))
                .willReturn(new UploadedVideoUrlQuery(null, null, null, null));

        // when
        FeedQuery info = facadeFeedService.getById(savedFeed.getId());

        // then
        assertThat(info).isNotNull();
        assertThat(info.id()).isEqualTo(savedFeed.getId());
        assertThat(info.clubProfileQuery().name()).isEqualTo(savedClub.getName());
        assertThat(info.activityContent()).isEqualTo(savedFeed.getActivityContent());
        assertThat(info.feedType()).isEqualTo(savedFeed.getFeedType().toString());
        assertThat(info.createdDate()).isEqualTo(LocalDate.from(now));
    }

    @DisplayName("피드 상세 조회 시 조회수가 1 증가한다.")
    @Test
    void getById_increments_viewCount() {
        // given
        Club club = fixture.giveMeBuilder(Club.class)
                .setNull("id")
                .set("name", "카우")
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sample();
        Club savedClub = clubRepository.save(club);

        DomainType clubDomainType = DomainType.CLUB_PROFILE;
        UUID id1 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.save(
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", id1)
                        .set("domainType", clubDomainType)
                        .set("entityId", savedClub.getId())
                        .set("fileStatus", FileStatus.COUPLED)
                        .sample()
        );

        Feed feed = fixture.giveMeBuilder(Feed.class)
                .setNull("id")
                .set("club", savedClub)
                .set("activityContent", "활동 내용")
                .set("feedType", FeedType.IMAGE)
                .set("viewCount", 0L)
                .set("createdAt", LocalDateTime.now())
                .sample();
        Feed savedFeed = feedRepository.save(feed);

        DomainType domainType = DomainType.FEED_IMAGE;
        UUID id2 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.save(
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", id2)
                        .set("domainType", domainType)
                        .set("entityId", savedFeed.getId())
                        .set("fileStatus", FileStatus.COUPLED)
                        .sample()
        );

        BDDMockito.given(s3FileService.getUploadedFileUrl(any()))
                .willReturn(new UploadedFileUrlQuery(null, null, null));
        BDDMockito.given(s3FileService.getUploadedFileUrlAndName(any(), any()))
                .willReturn(new UploadedFileUrlAndNameQuery(null, null, null, null));
        BDDMockito.given(s3FileService.getUploadedVideoUrl(any()))
                .willReturn(new UploadedVideoUrlQuery(null, null, null, null));

        // when
        facadeFeedService.getById(savedFeed.getId());

        // then
        Feed updatedFeed = feedRepository.findById(savedFeed.getId()).orElseThrow();
        assertThat(updatedFeed.getViewCount()).isEqualTo(1L);
    }

    @DisplayName("피드 상세 조회를 여러 번 하면 조회수가 누적된다.")
    @Test
    void getById_increments_viewCount_multiple_times() {
        // given
        Club club = fixture.giveMeBuilder(Club.class)
                .setNull("id")
                .set("name", "카우")
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sample();
        Club savedClub = clubRepository.save(club);

        DomainType clubDomainType = DomainType.CLUB_PROFILE;
        UUID id1 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.save(
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", id1)
                        .set("domainType", clubDomainType)
                        .set("entityId", savedClub.getId())
                        .set("fileStatus", FileStatus.COUPLED)
                        .sample()
        );

        Feed feed = fixture.giveMeBuilder(Feed.class)
                .setNull("id")
                .set("club", savedClub)
                .set("activityContent", "활동 내용")
                .set("feedType", FeedType.IMAGE)
                .set("viewCount", 0L)
                .set("createdAt", LocalDateTime.now())
                .sample();
        Feed savedFeed = feedRepository.save(feed);

        DomainType domainType = DomainType.FEED_IMAGE;
        UUID id2 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.save(
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", id2)
                        .set("domainType", domainType)
                        .set("entityId", savedFeed.getId())
                        .set("fileStatus", FileStatus.COUPLED)
                        .sample()
        );

        BDDMockito.given(s3FileService.getUploadedFileUrl(any()))
                .willReturn(new UploadedFileUrlQuery(null, null, null));
        BDDMockito.given(s3FileService.getUploadedFileUrlAndName(any(), any()))
                .willReturn(new UploadedFileUrlAndNameQuery(null, null, null, null));
        BDDMockito.given(s3FileService.getUploadedVideoUrl(any()))
                .willReturn(new UploadedVideoUrlQuery(null, null, null, null));

        // when
        facadeFeedService.getById(savedFeed.getId());
        facadeFeedService.getById(savedFeed.getId());
        facadeFeedService.getById(savedFeed.getId());

        // then
        Feed updatedFeed = feedRepository.findById(savedFeed.getId()).orElseThrow();
        assertThat(updatedFeed.getViewCount()).isEqualTo(3L);
    }
}
