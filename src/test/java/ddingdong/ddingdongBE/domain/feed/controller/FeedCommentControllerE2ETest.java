package ddingdong.ddingdongBE.domain.feed.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.auth.controller.dto.request.SignInRequest;
import ddingdong.ddingdongBE.auth.controller.dto.response.SignInResponse;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.NonTxTestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedComment;
import ddingdong.ddingdongBE.domain.feed.repository.FeedCommentRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeedCommentControllerE2ETest extends NonTxTestContainerSupport {

    private static final String VALID_UUID = "550e8400-e29b-41d4-a716-446655440000";

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedCommentRepository feedCommentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Feed feed;
    private String clubToken;

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void setUp() {
        RestAssured.port = port;

        User clubUser = userRepository.save(UserFixture.createClubUser(passwordEncoder.encode("1234")));
        Club club = clubRepository.save(ClubFixture.createClub(clubUser));
        feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));

        clubToken = signIn(clubUser.getAuthId(), "1234");
    }

    @DisplayName("비회원이 댓글을 작성하면 201과 commentId, anonymousNumber를 반환한다.")
    @Test
    void createComment_success() {
        Map<?, ?> response = given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", VALID_UUID)
                .body(Map.of("content", "테스트 댓글입니다."))
                .when()
                .post("/server/feeds/{feedId}/comments", feed.getId())
                .then()
                .statusCode(201)
                .extract()
                .as(Map.class);

        assertThat(response.get("commentId")).isNotNull();
        assertThat(response.get("anonymousNumber")).isEqualTo(1);
    }

    @DisplayName("본인 UUID로 댓글을 삭제하면 204를 반환한다.")
    @Test
    void deleteComment_success() {
        FeedComment comment = feedCommentRepository.save(
                FeedFixture.createFeedComment(feed, VALID_UUID, 1, "삭제할 댓글"));

        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", VALID_UUID)
                .when()
                .delete("/server/feeds/{feedId}/comments/{commentId}", feed.getId(), comment.getId())
                .then()
                .statusCode(204);

        assertThat(feedCommentRepository.findById(comment.getId())).isEmpty();
    }

    @DisplayName("ROLE_CLUB 동아리 회장이 댓글을 강제삭제하면 204를 반환한다.")
    @Test
    void forceDeleteComment_success() {
        FeedComment comment = feedCommentRepository.save(
                FeedFixture.createFeedComment(feed, VALID_UUID, 1, "삭제 대상 댓글"));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + clubToken)
                .when()
                .delete("/server/central/feeds/{feedId}/comments/{commentId}", feed.getId(), comment.getId())
                .then()
                .statusCode(204);

        assertThat(feedCommentRepository.findById(comment.getId())).isEmpty();
    }

    private String signIn(String authId, String password) {
        SignInResponse response = given()
                .contentType(ContentType.JSON)
                .body(new SignInRequest(authId, password))
                .when()
                .post("/server/auth/sign-in")
                .then()
                .statusCode(200)
                .extract()
                .as(SignInResponse.class);
        return response.getToken();
    }
}
