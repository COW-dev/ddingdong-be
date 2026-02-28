package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.github.f4b6a3.uuid.UuidCreator;
import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.fixture.FileMetaDataFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.entity.PhoneNumber;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedType;
import ddingdong.ddingdongBE.domain.feed.repository.FeedCommentRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.command.UpdateFeedCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.MyFeedPageQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataServiceImpl;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class FacadeClubFeedServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeClubFeedService facadeClubFeedService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private FeedCommentRepository feedCommentRepository;
    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private S3FileService s3FileService;

    private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();
    @Autowired
    private FileMetaDataServiceImpl fileMetaDataServiceImpl;

    @DisplayName("요청된 Command를 사용하여 feed를 생성하며, FileMetaData를 Couple 상태로 변경한다.")
    @Test
    void create() {
        // given
        User savedUser = userRepository.save(fixtureMonkey.giveMeBuilder(User.class).set("id", null).sample());
        Club club = fixtureMonkey.giveMeBuilder(Club.class)
            .setNull("id")
            .set("user", savedUser)
            .set("score", Score.from(BigDecimal.ZERO))
            .set("phoneNumber", PhoneNumber.from("010-1234-5678"))
            .set("location", Location.from("S1111"))
            .set("clubMembers", null)
            .set("deletedAt", null)
            .sample();
        clubRepository.save(club);
        UUID id1 = UuidCreator.getTimeOrderedEpoch();
        CreateFeedCommand command = fixtureMonkey.giveMeBuilder(CreateFeedCommand.class)
            .set("mimeType", "image/png")
            .set("mediaId", id1.toString())
            .set("user", savedUser)
            .sample();
        fileMetaDataRepository.save(
            fixtureMonkey.giveMeBuilder(FileMetaData.class)
                .set("id", id1)
                .set("fileStatus", FileStatus.PENDING)
                .sample()
        );
        // when
        facadeClubFeedService.create(command);
        // then
        FileMetaData fileMetaData = fileMetaDataRepository.findById(id1).orElse(null);
        assertThat(fileMetaData).isNotNull();
        assertThat(fileMetaData.getDomainType()).isEqualTo(DomainType.FEED_IMAGE);
        assertThat(fileMetaData.getFileStatus()).isEqualTo(FileStatus.COUPLED);

        Feed feed = feedRepository.findById(fileMetaData.getEntityId()).orElse(null);
        assertThat(feed).isNotNull();
        assertThat(feed.getFeedType()).isEqualTo(FeedType.IMAGE);
    }

    @DisplayName("요청된 Command를 사용하여 feed를 수정한다")
    @Test
    void update() {
        // given
        Feed savedFeed = feedRepository.save(
            fixtureMonkey.giveMeBuilder(Feed.class)
                .setNull("id")
                .set("activityContent", "기존 활동내용")
                .set("feedType", FeedType.VIDEO)
                .set("club", null)
                .sample()
        );
        UpdateFeedCommand command = fixtureMonkey.giveMeBuilder(UpdateFeedCommand.class)
            .set("activityContent", "변경된 활동내용")
            .set("feedId", savedFeed.getId())
            .sample();
        // when
        facadeClubFeedService.update(command);
        entityManager.flush();
        // then
        Feed finded = feedRepository.findById(savedFeed.getId()).orElse(null);
        assertThat(finded).isNotNull();
        assertThat(finded.getActivityContent()).isEqualTo("변경된 활동내용");
    }

    @DisplayName("주어진 feedId를 가진 Feed 엔터티를 삭제 및 fileMetaData 상태를 DELETED로 변경 - IMAGE")
    @Test
    void deleteImage() {
        // given
        UUID uuid = UuidCreator.getTimeOrderedEpoch();


        Feed savedFeed = feedRepository.save(
            fixtureMonkey.giveMeBuilder(Feed.class)
                .setNull("id")
                .set("feedType", FeedType.IMAGE)
                .set("activityContent", "활동내용")
                .set("club", null)
                .sample()
        );
        fileMetaDataRepository.save(
            fixtureMonkey.giveMeBuilder(FileMetaData.class)
                .set("id", uuid)
                .set("entityId", savedFeed.getId())
                .set("domainType", DomainType.FEED_IMAGE)
                .set("fileStatus", FileStatus.COUPLED)
                .sample()
        );
        entityManager.flush();
        // when
        facadeClubFeedService.delete(savedFeed.getId());
        entityManager.flush();
        // then
        Feed feed = feedRepository.findById(savedFeed.getId()).orElse(null);
        FileMetaData fileMetaData = fileMetaDataRepository.findById(uuid).orElse(null);
        assertThat(feed).isNull();
        assertThat(fileMetaData).isNotNull();
        assertThat(fileMetaData.getFileStatus()).isEqualTo(FileStatus.DELETED);
    }

    @DisplayName("내 피드 목록 조회 시 각 피드의 조회수, 좋아요 수, 댓글 수가 포함된다")
    @Test
    void getMyFeedPage_WithViewCountLikeCountCommentCount() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));

        Feed feed1 = feedRepository.save(
                Feed.builder()
                        .club(savedClub)
                        .feedType(FeedType.IMAGE)
                        .activityContent("피드1")
                        .viewCount(10)
                        .likeCount(0)
                        .build()
        );
        Feed feed2 = feedRepository.save(
                Feed.builder()
                        .club(savedClub)
                        .feedType(FeedType.IMAGE)
                        .activityContent("피드2")
                        .viewCount(20)
                        .likeCount(0)
                        .build()
        );

        UUID fileId1 = UuidCreator.getTimeOrderedEpoch();
        UUID fileId2 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.save(FileMetaDataFixture.create(fileId1, feed1.getId(), DomainType.FEED_IMAGE));
        fileMetaDataRepository.save(FileMetaDataFixture.create(fileId2, feed2.getId(), DomainType.FEED_IMAGE));

        feedRepository.addLikeCount(feed1.getId(), 1);
        feedRepository.addLikeCount(feed1.getId(), 1);
        feedRepository.addLikeCount(feed2.getId(), 1);

        feedCommentRepository.save(FeedFixture.createFeedComment(feed1, "uuid-1", 1, "댓글1"));
        feedCommentRepository.save(FeedFixture.createFeedComment(feed1, "uuid-2", 2, "댓글2"));
        feedCommentRepository.save(FeedFixture.createFeedComment(feed2, "uuid-3", 1, "댓글3"));

        BDDMockito.given(s3FileService.getUploadedFileUrl(any()))
                .willReturn(new UploadedFileUrlQuery(null, null, null));

        entityManager.flush();
        entityManager.clear();

        // when
        MyFeedPageQuery result = facadeClubFeedService.getMyFeedPage(savedUser, 10, -1L);

        // then
        List<FeedListQuery> feeds = result.feedListQueries();
        assertThat(feeds).hasSize(2);

        FeedListQuery feedQuery1 = feeds.stream().filter(f -> f.id().equals(feed1.getId())).findFirst().orElseThrow();
        assertThat(feedQuery1.viewCount()).isEqualTo(10);
        assertThat(feedQuery1.likeCount()).isEqualTo(2);
        assertThat(feedQuery1.commentCount()).isEqualTo(2);

        FeedListQuery feedQuery2 = feeds.stream().filter(f -> f.id().equals(feed2.getId())).findFirst().orElseThrow();
        assertThat(feedQuery2.viewCount()).isEqualTo(20);
        assertThat(feedQuery2.likeCount()).isEqualTo(1);
        assertThat(feedQuery2.commentCount()).isEqualTo(1);
    }

    @DisplayName("주어진 feedId를 가진 Feed 엔터티를 삭제 및 fileMetaData 상태를 DELETED로 변경 - VIDEO")
    @Test
    void deleteVideo() {
        // given
        UUID uuid = UuidCreator.getTimeOrderedEpoch();


        Feed savedFeed = feedRepository.save(
            fixtureMonkey.giveMeBuilder(Feed.class)
                .setNull("id")
                .set("feedType", FeedType.VIDEO)
                .set("activityContent", "활동내용")
                .set("club", null)
                .sample()
        );
        fileMetaDataRepository.save(
            fixtureMonkey.giveMeBuilder(FileMetaData.class)
                .set("id", uuid)
                .set("entityId", savedFeed.getId())
                .set("domainType", DomainType.FEED_VIDEO)
                .set("fileStatus", FileStatus.COUPLED)
                .sample()
        );
        entityManager.flush();
        // when
        facadeClubFeedService.delete(savedFeed.getId());
        entityManager.flush();
        // then
        Feed feed = feedRepository.findById(savedFeed.getId()).orElse(null);
        FileMetaData fileMetaData = fileMetaDataRepository.findById(uuid).orElse(null);
        assertThat(feed).isNull();
        assertThat(fileMetaData).isNotNull();
        assertThat(fileMetaData.getFileStatus()).isEqualTo(FileStatus.DELETED);
    }
}
