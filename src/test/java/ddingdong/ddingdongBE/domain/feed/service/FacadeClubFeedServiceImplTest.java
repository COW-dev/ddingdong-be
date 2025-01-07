package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.f4b6a3.uuid.UuidCreator;
import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.entity.PhoneNumber;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedType;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.command.UpdateFeedCommand;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    private EntityManager entityManager;

    private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("요청된 Command를 사용하여 feed를 생성하며, FileMetaData를 Couple 상태로 변경한다.")
    @Test
    void create() {
        // given
        User savedUser = userRepository.save(fixtureMonkey.giveMeOne(User.class));
        Club club = fixtureMonkey.giveMeBuilder(Club.class)
            .set("id", 1L)
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
            .set("contentType", "IMAGE")
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

    @DisplayName("요청된 Command를 사용하여 feed를 수정하며, FileMetaData의 id및 domainType을 변경한다.")
    @Test
    void update() {
        // given
        Feed savedFeed = feedRepository.save(
            fixtureMonkey.giveMeBuilder(Feed.class)
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
}
