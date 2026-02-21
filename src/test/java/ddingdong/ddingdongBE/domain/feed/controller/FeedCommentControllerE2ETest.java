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
    private static final String ANOTHER_UUID = "660f9511-f30c-42e5-a827-557766551111";
    private static final String INVALID_UUID = "not-a-valid-uuid";

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

    @DisplayName("동일한 UUID로 재방문 시 기존 anonymousNumber를 재사용한다.")
    @Test
    void createComment_sameUuidReusesAnonymousNumber() {
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", VALID_UUID)
                .body(Map.of("content", "첫 번째 댓글"))
                .when()
                .post("/server/feeds/{feedId}/comments", feed.getId())
                .then()
                .statusCode(201);

        Map<?, ?> response = given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", VALID_UUID)
                .body(Map.of("content", "두 번째 댓글"))
                .when()
                .post("/server/feeds/{feedId}/comments", feed.getId())
                .then()
                .statusCode(201)
                .extract()
                .as(Map.class);

        assertThat(response.get("anonymousNumber")).isEqualTo(1);
    }

    @DisplayName("다른 UUID는 순차적으로 익명 번호를 부여받는다.")
    @Test
    void createComment_differentUuidGetsNextAnonymousNumber() {
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", VALID_UUID)
                .body(Map.of("content", "첫 번째 댓글"))
                .when()
                .post("/server/feeds/{feedId}/comments", feed.getId())
                .then()
                .statusCode(201);

        Map<?, ?> response = given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", ANOTHER_UUID)
                .body(Map.of("content", "두 번째 댓글"))
                .when()
                .post("/server/feeds/{feedId}/comments", feed.getId())
                .then()
                .statusCode(201)
                .extract()
                .as(Map.class);

        assertThat(response.get("anonymousNumber")).isEqualTo(2);
    }

    @DisplayName("content가 빈 값이면 400을 반환한다.")
    @Test
    void createComment_fail_blankContent() {
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", VALID_UUID)
                .body(Map.of("content", ""))
                .when()
                .post("/server/feeds/{feedId}/comments", feed.getId())
                .then()
                .statusCode(400);
    }

    @DisplayName("UUID 형식이 올바르지 않으면 400을 반환한다.")
    @Test
    void createComment_fail_invalidUuid() {
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", INVALID_UUID)
                .body(Map.of("content", "댓글"))
                .when()
                .post("/server/feeds/{feedId}/comments", feed.getId())
                .then()
                .statusCode(400);
    }

    @DisplayName("존재하지 않는 피드에 댓글 작성 시 404를 반환한다.")
    @Test
    void createComment_fail_feedNotFound() {
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", VALID_UUID)
                .body(Map.of("content", "댓글"))
                .when()
                .post("/server/feeds/{feedId}/comments", 9999L)
                .then()
                .statusCode(404);
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

    @DisplayName("타인 UUID로 댓글 삭제 시 403을 반환한다.")
    @Test
    void deleteComment_fail_otherUuid() {
        FeedComment comment = feedCommentRepository.save(
                FeedFixture.createFeedComment(feed, VALID_UUID, 1, "남의 댓글"));

        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", ANOTHER_UUID)
                .when()
                .delete("/server/feeds/{feedId}/comments/{commentId}", feed.getId(), comment.getId())
                .then()
                .statusCode(403);
    }

    @DisplayName("존재하지 않는 댓글 삭제 시 404를 반환한다.")
    @Test
    void deleteComment_fail_notFound() {
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", VALID_UUID)
                .when()
                .delete("/server/feeds/{feedId}/comments/{commentId}", feed.getId(), 9999L)
                .then()
                .statusCode(404);
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

    @DisplayName("미인증 사용자가 강제삭제 시 401을 반환한다.")
    @Test
    void forceDeleteComment_fail_unauthenticated() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/server/central/feeds/{feedId}/comments/{commentId}", feed.getId(), 1L)
                .then()
                .statusCode(401);
    }

    @DisplayName("ROLE_ADMIN 사용자가 강제삭제 시도 시 403을 반환한다.")
    @Test
    void forceDeleteComment_fail_nonClubRole() {
        userRepository.save(UserFixture.createAdminUser(passwordEncoder.encode("1234")));
        String adminToken = signIn("admin123", "1234");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .delete("/server/central/feeds/{feedId}/comments/{commentId}", feed.getId(), 1L)
                .then()
                .statusCode(403);
    }
}
