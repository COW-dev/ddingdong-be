package ddingdong.ddingdongBE.domain.feed.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import ddingdong.ddingdongBE.auth.controller.dto.request.SignInRequest;
import ddingdong.ddingdongBE.auth.controller.dto.response.SignInResponse;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.NonTxTestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedLikeRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminFeedControllerE2ETest extends NonTxTestContainerSupport {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedLikeRepository feedLikeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String adminToken;

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void setUp() {
        RestAssured.port = port;

        User adminUser = userRepository.save(
                UserFixture.createAdminUser(passwordEncoder.encode("1234")));
        adminToken = signIn(adminUser.getAuthId(), "1234");
    }

    @DisplayName("역대 1위 동아리 조회 - 성공: ADMIN 인증으로 200과 단건 응답을 반환한다.")
    @Test
    void getYearlyWinner_success() {
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동"));

        jdbcTemplate.update("UPDATE feed SET created_at = ? WHERE id = ?",
                LocalDateTime.of(2025, 6, 15, 10, 0), feed.getId());

        feedLikeRepository.save(FeedFixture.createFeedLike(feed, UUID.randomUUID().toString()));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/server/admin/feeds/ranking/last?year=2025")
                .then()
                .statusCode(200)
                .body("clubName", equalTo("컴퓨터공학과 동아리"))
                .body("feedCount", equalTo(1))
                .body("viewCount", equalTo(0))
                .body("likeCount", equalTo(1))
                .body("commentCount", equalTo(0))
                .body("score", equalTo(13))
                .body("targetYear", equalTo(2025))
                .body("targetMonth", equalTo(6));
    }

    @DisplayName("역대 1위 동아리 조회 - 실패: 피드가 없으면 404를 반환한다.")
    @Test
    void getYearlyWinner_not_found() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/server/admin/feeds/ranking/last?year=2025")
                .then()
                .statusCode(404);
    }

    @DisplayName("역대 1위 동아리 조회 - 실패: 미인증 접근 시 401을 반환한다.")
    @Test
    void getYearlyWinner_unauthorized() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/server/admin/feeds/ranking/last?year=2025")
                .then()
                .statusCode(401);
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
