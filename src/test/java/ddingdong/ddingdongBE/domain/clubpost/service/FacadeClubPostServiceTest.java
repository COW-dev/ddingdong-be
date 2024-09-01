package ddingdong.ddingdongBE.domain.clubpost.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.f4b6a3.uuid.UuidCreator;
import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubFeedResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubPostListResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubPostResponse;
import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import ddingdong.ddingdongBE.domain.clubpost.repository.ClubPostRepository;
import ddingdong.ddingdongBE.domain.clubpost.service.dto.CreateClubPostCommand;
import ddingdong.ddingdongBE.domain.clubpost.service.dto.UpdateClubPostCommand;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import ddingdong.ddingdongBE.file.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.FileMetaDataCommand;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeClubPostServiceTest extends TestContainerSupport {

  @Autowired
  private FacadeClubPostService facadeClubPostService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ClubRepository clubRepository;

  @Autowired
  private ClubPostRepository clubPostRepository;

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private S3FileService s3FileService;

  @Autowired
  private FileMetaDataService fileMetaDataService;

  private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getBuilderIntrospectorNotNullMonkey();
  @Autowired
  private ClubPostService clubPostService;

  @DisplayName("게시물을 생성할 수 있습니다.")
  @Test
  void createClubPost() {
    // given
    User user = fixtureMonkey.giveMeOne(User.class);
    User savedUser = userRepository.save(user);
    Club club = fixtureMonkey.giveMeBuilder(Club.class)
        .set("user", savedUser)
        .set("score", Score.from(0))
        .set("clubMembers", null)
        .sample();
    clubRepository.save(club);
    clubRepository.flush();

    CreateClubPostCommand command = fixtureMonkey.giveMeBuilder(CreateClubPostCommand.class)
        .set("userId", savedUser.getId())
        .set("fileMetaDataCommand", new FileMetaDataCommand(UuidCreator.getTimeOrderedEpoch(), "example.png"))
        .sample();
    // when
    facadeClubPostService.create(command);
    // then
    ClubPost findClubPost = clubPostRepository.findById(1L)
        .orElse(null);
    FileMetaDataCommand fileMetaDataCommand = command.fileMetaDataCommand();
    String fileUrl = s3FileService.getUploadedFileUrl(fileMetaDataCommand.fileName(), fileMetaDataCommand.fileId());
    assertThat(findClubPost).isNotNull();
    assertThat(findClubPost.getActivityContent()).isEqualTo(command.activityContent());
    assertThat(findClubPost.getFileUrl()).isEqualTo(fileUrl);
  }

  @DisplayName("게시물을 수정할 수 있습니다.")
  @Test
  void updateClubPost() {
    // given
    ClubPost clubPost = fixtureMonkey.giveMeBuilder(ClubPost.class)
        .set("club", null)
        .sample();
    ClubPost savedPost = clubPostRepository.save(clubPost);

    UpdateClubPostCommand updateClubPostCommand = fixtureMonkey.giveMeBuilder(UpdateClubPostCommand.class)
        .set("clubPostId", savedPost.getId())
        .set("fileMetaDataCommand", new FileMetaDataCommand(UuidCreator.getTimeOrderedEpoch(), "example.png")

        )
        .sample();
    // when
    facadeClubPostService.update(updateClubPostCommand);
    clubPostRepository.flush();
    // then
    ClubPost findClubPost = clubPostRepository.findById(savedPost.getId()).orElse(null);
    FileMetaDataCommand fileMetaDataCommand = updateClubPostCommand.fileMetaDataCommand();
    String fileUrl = s3FileService.getUploadedFileUrl(fileMetaDataCommand.fileName(), fileMetaDataCommand.fileId());
    assertThat(findClubPost).isNotNull();
    assertThat(findClubPost.getActivityContent()).isEqualTo(updateClubPostCommand.activityContent());
    assertThat(findClubPost.getFileUrl()).isEqualTo(fileUrl);
  }

  @DisplayName("게시물을 삭제할 수 있습니다.")
  @Test
  void deleteClubPost() {
    // given
    ClubPost clubPost = fixtureMonkey.giveMeBuilder(ClubPost.class)
        .set("club", null)
        .sample();
    ClubPost savedPost = clubPostRepository.save(clubPost);
    // when
    Long clubPostId = savedPost.getId();
    facadeClubPostService.delete(clubPostId);
    clubPostRepository.flush();
    // then
    ClubPost findClubPost = (ClubPost) entityManager.createNativeQuery("select * from club_post limit 1", ClubPost.class)
        .getSingleResult();
    assertThat(findClubPost).isNotNull();
    assertThat(findClubPost.getDeletedAt()).isNotNull();
  }

  @DisplayName("게시물을 전체 조회할 수 있습니다.")
  @Test
  void getAllClubPosts() {
    // given
    Club club = fixtureMonkey.giveMeBuilder(Club.class)
        .set("name", "카우")
        .set("score", Score.from(0))
        .set("user", null)
        .set("clubMembers", null)
        .sample();
    Club savedclub = clubRepository.save(club);

    ClubPost post1 = fixtureMonkey.giveMeBuilder(ClubPost.class)
        .set("club", savedclub)
        .sample();
    ClubPost post2 = fixtureMonkey.giveMeBuilder(ClubPost.class)
        .set("club", savedclub)
        .sample();;
    ClubPost savedPost1 = clubPostRepository.save(post1);
    ClubPost savedPost2 = clubPostRepository.save(post2);
    // when
    ClubPostListResponse response = facadeClubPostService.getAllByClubId(savedclub.getId());
    // then
    assertThat(response).isNotNull();
    assertThat(response.clubPostFiles()).size().isEqualTo(2);
    assertThat(response.clubPostFiles().get(0).fileUrl()).isEqualTo(savedPost1.getFileUrl());
    assertThat(response.clubPostFiles().get(1).fileUrl()).isEqualTo(savedPost2.getFileUrl());
  }

  @DisplayName("게시물을 상세 조회할 수 있다.")
  @Test
  void getClubPost() {
    // given
    Club club = fixtureMonkey.giveMeBuilder(Club.class)
        .set("name", "카우")
        .set("score", Score.from(0))
        .set("user", null)
        .set("clubMembers", null)
        .sample();
    Club savedclub = clubRepository.save(club);
    ClubPost post = fixtureMonkey.giveMeBuilder(ClubPost.class)
        .set("club", savedclub)
        .sample();
    ClubPost savedPost = clubPostRepository.save(post);
    // when
    ClubPostResponse response = facadeClubPostService.getByClubPostId(savedPost.getId());
    // then
    assertThat(response).isNotNull();
    assertThat(response.name()).isEqualTo("카우");
    assertThat(response.clubPostFile().fileUrl()).isEqualTo(savedPost.getFileUrl());
    assertThat(response.profileImageFile().fileUrl()).isEqualTo(savedclub.getProfileImageUrl());
    assertThat(response.activityContent()).isEqualTo(savedPost.getActivityContent());
    assertThat(response.createdDate()).isEqualTo(LocalDate.from(savedPost.getCreatedAt()));
  }

  @DisplayName("각 동아리의 최신 게시물을 모아놓은 피드를 조회할 수 있다.")
  @Test
  void findNewestPostsByClub() {
    // given
    UUID fileId = UuidCreator.getTimeOrderedEpoch();
    String fileName = "example.png";
    String fileUrl = s3FileService.getUploadedFileUrl(fileName, fileId);
    Club club1 = fixtureMonkey.giveMeBuilder(Club.class)
        .set("name", "테스트1")
        .set("score", Score.from(0))
        .set("user", null)
        .set("clubMembers", null)
        .sample();
    Club club2 = fixtureMonkey.giveMeBuilder(Club.class)
        .set("name", "테스트2")
        .set("score", Score.from(0))
        .set("user", null)
        .set("clubMembers", null)
        .sample();
    Club club3 = fixtureMonkey.giveMeBuilder(Club.class)
        .set("name", "테스트3")
        .set("score", Score.from(0))
        .set("user", null)
        .set("clubMembers", null)
        .sample();
    Club savedClub1 = clubRepository.save(club1);
    Club savedClub2 = clubRepository.save(club2);
    Club savedClub3 = clubRepository.save(club3);

    ClubPost post1 = fixtureMonkey.giveMeBuilder(ClubPost.class)
        .set("club", savedClub1)
        .set("fileUrl", fileUrl)
        .sample();
    ClubPost post2 = fixtureMonkey.giveMeBuilder(ClubPost.class)
        .set("club", savedClub1)
        .set("fileUrl", fileUrl)
        .sample();
    ClubPost post3 = fixtureMonkey.giveMeBuilder(ClubPost.class)
        .set("club", savedClub2)
        .set("fileUrl", fileUrl)
        .sample();
    ClubPost post4 = fixtureMonkey.giveMeBuilder(ClubPost.class)
        .set("club", savedClub2)
        .set("fileUrl", fileUrl)
        .sample();
    ClubPost post5 = fixtureMonkey.giveMeBuilder(ClubPost.class)
        .set("club", savedClub3)
        .set("fileUrl", fileUrl)
        .sample();
    ClubPost post6 = fixtureMonkey.giveMeBuilder(ClubPost.class)
        .set("club", savedClub3)
        .set("fileUrl", fileUrl)
        .sample();;

    clubPostRepository.saveAll(List.of(post1, post2, post3, post4, post5, post6));
    clubPostRepository.flush();
    // when
    ClubFeedResponse response = facadeClubPostService.findAllRecentPostByClub();
    // then
    assertThat(response).isNotNull();
    assertThat(response.clubPostFiles()).size().isEqualTo(3);
    assertThat(response.clubPostFiles().get(0).fileUrl()).isEqualTo(post1.getFileUrl());
    assertThat(response.clubPostFiles().get(0).fileUrl()).isEqualTo(post3.getFileUrl());
    assertThat(response.clubPostFiles().get(0).fileUrl()).isEqualTo(post5.getFileUrl());
  }
}