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
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.AdminClubFeedRankingResponse;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedLikeRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
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

    private String adminToken;

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void setUp() {
        RestAssured.port = port;

        User admin = userRepository.save(
                UserFixture.createAdminUser(passwordEncoder.encode("1234")));

        adminToken = getAuthToken(admin.getAuthId(), "1234");
    }

    @DisplayName("총동연 피드 랭킹 조회 API - 성공: 점수 순으로 랭킹이 반환된다")
    @Test
    void getClubFeedRanking_success() {
        // given
        Club clubA = clubRepository.save(ClubFixture.createClub("동아리A"));
        Club clubB = clubRepository.save(ClubFixture.createClub("동아리B"));

        // 동아리A: 피드 1개 → score = 10
        feedRepository.save(FeedFixture.createImageFeed(clubA, "피드A"));

        // 동아리B: 피드 2개 + 좋아요 1개 → score = 2*10 + 1*3 = 23
        Feed feedB = feedRepository.save(FeedFixture.createImageFeed(clubB, "피드B1"));
        feedRepository.save(FeedFixture.createImageFeed(clubB, "피드B2"));
        feedLikeRepository.save(FeedFixture.createFeedLike(feedB, "uuid-1"));

        int year = java.time.LocalDate.now().getYear();
        int month = java.time.LocalDate.now().getMonthValue();

        // when & then
        List<AdminClubFeedRankingResponse> response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("year", year)
                .queryParam("month", month)
                .when()
                .get("/server/admin/feeds/ranking")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", AdminClubFeedRankingResponse.class);

        assertThat(response).hasSize(2);

        AdminClubFeedRankingResponse first = response.get(0);
        assertThat(first.clubName()).isEqualTo("동아리B");
        assertThat(first.rank()).isEqualTo(1);
        assertThat(first.feedScore()).isEqualTo(2 * 10);
        assertThat(first.viewScore()).isEqualTo(0);
        assertThat(first.likeScore()).isEqualTo(1 * 3);
        assertThat(first.commentScore()).isEqualTo(0);
        assertThat(first.totalScore()).isEqualTo(23);

        AdminClubFeedRankingResponse second = response.get(1);
        assertThat(second.clubName()).isEqualTo("동아리A");
        assertThat(second.rank()).isEqualTo(2);
        assertThat(second.feedScore()).isEqualTo(1 * 10);
        assertThat(second.totalScore()).isEqualTo(10);
    }

    @DisplayName("총동연 피드 랭킹 조회 API - 성공: 데이터 없으면 빈 리스트 반환")
    @Test
    void getClubFeedRanking_empty() {
        List<AdminClubFeedRankingResponse> response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("year", 2000)
                .queryParam("month", 1)
                .when()
                .get("/server/admin/feeds/ranking")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", AdminClubFeedRankingResponse.class);

        assertThat(response).isEmpty();
    }

    @DisplayName("총동연 피드 랭킹 조회 API - 실패: month 파라미터 유효성 검증 (0 이하)")
    @Test
    void getClubFeedRanking_invalidMonth() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("year", 2025)
                .queryParam("month", 0)
                .when()
                .get("/server/admin/feeds/ranking")
                .then()
                .statusCode(400);
    }

    private String getAuthToken(String authId, String password) {
        SignInResponse signInResponse = given()
                .contentType(ContentType.JSON)
                .body(new SignInRequest(authId, password))
                .when()
                .post("/server/auth/sign-in")
                .then()
                .statusCode(200)
                .extract()
                .as(SignInResponse.class);
        return signInResponse.getToken();
    }
}
