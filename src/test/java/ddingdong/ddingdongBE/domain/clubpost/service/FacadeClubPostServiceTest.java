package ddingdong.ddingdongBE.domain.clubpost.service;

import static org.assertj.core.api.Assertions.assertThat;

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
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
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

  private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getBuilderIntrospectorMonkey();
  @Autowired
  private ClubPostService clubPostService;

  @DisplayName("게시물을 생성할 수 있습니다.")
  @Test
  void createClubPost() {
    // given
    User user = fixtureMonkey.giveMeOne(User.class);
    User savedUser = userRepository.save(user);
    userRepository.flush();
    Club club = fixtureMonkey.giveMeBuilder(Club.class).set("user", savedUser).sample();
    clubRepository.save(club);
    clubRepository.flush();

    CreateClubPostCommand command = fixtureMonkey.giveMeBuilder(CreateClubPostCommand.class)
        .set("userId", savedUser.getId())
        .sample();
    // when
    facadeClubPostService.create(command);
    // then
    ClubPost findClubPost = clubPostRepository.findById(1L)
        .orElse(null);
    assertThat(findClubPost).isNotNull();
    assertThat(findClubPost.getActivityContent()).isEqualTo(command.activityContent());
    assertThat(findClubPost.getFileUrl()).isEqualTo(command.fileId());
  }

  @DisplayName("게시물을 수정할 수 있습니다.")
  @Test
  void updateClubPost() {
    // given
    ClubPost clubPost = fixtureMonkey.giveMeOne(ClubPost.class);
    ClubPost savedPost = clubPostRepository.save(clubPost);

    UpdateClubPostCommand updateClubPostCommand = fixtureMonkey.giveMeBuilder(UpdateClubPostCommand.class)
        .set("clubPostId", savedPost.getId())
        .sample();
    // when
    clubPostService.update(updateClubPostCommand);
    clubPostRepository.flush();
    // then
    ClubPost findClubPost = clubPostRepository.findById(savedPost.getId()).orElse(null);
    assertThat(findClubPost).isNotNull();
    assertThat(findClubPost.getActivityContent()).isEqualTo(updateClubPostCommand.activityContent());
    assertThat(findClubPost.getFileUrl()).isEqualTo(updateClubPostCommand.mediaName());
  }

  @DisplayName("게시물을 삭제할 수 있습니다.")
  @Test
  void deleteClubPost() {
    // given
    ClubPost clubPost = fixtureMonkey.giveMeOne(ClubPost.class);
    ClubPost savedPost = clubPostRepository.save(clubPost);
    // when
    Long clubId = savedPost.getId();
    facadeClubPostService.delete(clubId);
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
    Club club = Club.builder()
        .name("카우")
        .build();
    Club savedclub = clubRepository.save(club);

    ClubPost post1 = fixtureMonkey.giveMeOne(ClubPost.class);
    ClubPost post2 = fixtureMonkey.giveMeOne(ClubPost.class);
    post1.updateClub(savedclub);
    post2.updateClub(savedclub);
    ClubPost savedPost1 = clubPostRepository.save(post1);
    ClubPost savedPost2 = clubPostRepository.save(post2);
    // when
    ClubPostListResponse response = facadeClubPostService.getRecentAllByClubId(savedclub.getId());
    // then
    assertThat(response).isNotNull();
    assertThat(response.mediaUrls()).size().isEqualTo(2);
    assertThat(response.mediaUrls().get(0)).isEqualTo(savedPost1.getFileUrl());
    assertThat(response.mediaUrls().get(1)).isEqualTo(savedPost2.getFileUrl());
  }

  @DisplayName("게시물을 상세 조회할 수 있다.")
  @Test
  void getClubPost() {
    // given
    Club club = Club.builder()
        .name("카우")
        .build();
    Club savedclub = clubRepository.save(club);
    ClubPost post = fixtureMonkey.giveMeOne(ClubPost.class);
    post.updateClub(savedclub);
    ClubPost savedPost = clubPostRepository.save(post);
    // when
    ClubPostResponse response = facadeClubPostService.getByClubPostId(savedPost.getId());
    // then
    assertThat(response).isNotNull();
    assertThat(response.clubName()).isEqualTo("카우");
    assertThat(response.mediaUrl()).isEqualTo(savedPost.getFileUrl());
    assertThat(response.activityContent()).isEqualTo(savedPost.getActivityContent());
    assertThat(response.createdDate()).isEqualTo(LocalDate.from(savedPost.getCreatedAt()));
  }

  @DisplayName("각 동아리의 최신 게시물을 모아놓은 피드를 조회할 수 있다.")
  @Test
  void findNewestPostsByClub() {
    // given
    Club club1 = Club.builder()
        .name("테스트1")
        .build();
    Club club2 = Club.builder()
        .name("테스트2")
        .build();
    Club club3 = Club.builder()
        .name("테스트3")
        .build();
    Club savedClub1 = clubRepository.save(club1);
    Club savedClub2 = clubRepository.save(club2);
    Club savedClub3 = clubRepository.save(club3);

    ClubPost post1 = fixtureMonkey.giveMeOne(ClubPost.class);
    ClubPost post2 = fixtureMonkey.giveMeOne(ClubPost.class);
    post1.updateClub(savedClub1);
    post2.updateClub(savedClub1);
    ClubPost post3 = fixtureMonkey.giveMeOne(ClubPost.class);
    ClubPost post4 = fixtureMonkey.giveMeOne(ClubPost.class);
    post3.updateClub(savedClub2);
    post4.updateClub(savedClub2);
    ClubPost post5 = fixtureMonkey.giveMeOne(ClubPost.class);
    ClubPost post6 = fixtureMonkey.giveMeOne(ClubPost.class);
    post5.updateClub(savedClub3);
    post6.updateClub(savedClub3);

    clubPostRepository.saveAll(List.of(post1, post2, post3, post4, post5, post6));
    // when
    ClubFeedResponse response = facadeClubPostService.findAllRecentPostByClub();
    // then
    assertThat(response).isNotNull();
    assertThat(response.fileUrls()).size().isEqualTo(3);
    assertThat(response.fileUrls().get(0)).isEqualTo(post1.getFileUrl());
    assertThat(response.fileUrls().get(1)).isEqualTo(post3.getFileUrl());
    assertThat(response.fileUrls().get(2)).isEqualTo(post5.getFileUrl());
  }
}