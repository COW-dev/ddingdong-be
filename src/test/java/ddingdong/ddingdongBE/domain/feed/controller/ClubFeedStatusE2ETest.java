package ddingdong.ddingdongBE.domain.feed.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.auth.controller.dto.request.SignInRequest;
import ddingdong.ddingdongBE.auth.controller.dto.response.SignInResponse;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.NonTxTestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedCommentRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedLikeRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClubFeedStatusE2ETest extends NonTxTestContainerSupport {

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
    private FeedCommentRepository feedCommentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private int year;
    private int month;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        year = LocalDate.now().getYear();
        month = LocalDate.now().getMonthValue();
    }

    @DisplayName("동아리 이달의 현황 조회 성공 - 피드 2개, 좋아요 1, 댓글 1")
    @Test
    void getFeedStatus_success() {
        // given
        User clubUser = userRepository.save(UserFixture.createClubUser(passwordEncoder.encode("1234")));
        Club club = clubRepository.save(ClubFixture.createClub(clubUser));

        Feed feed1 = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용 1"));
        Feed feed2 = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용 2"));
        feedLikeRepository.save(FeedFixture.createFeedLike(feed1, "uuid-1"));
        feedCommentRepository.save(FeedFixture.createFeedComment(feed2, "uuid-2", 1, "댓글"));

        String token = signIn("club123", "1234");

        // when & then
        // score = feedCount(2)*10 + viewCount(0)*1 + likeCount(1)*3 + commentCount(1)*5 = 28
        Map<?, ?> response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .queryParam("year", year)
                .queryParam("month", month)
                .when()
                .get("/server/central/feeds/status")
                .then()
                .statusCode(200)
                .extract()
                .as(Map.class);

        assertSoftly(softly -> {
            softly.assertThat(response.get("year")).isEqualTo(year);
            softly.assertThat(response.get("month")).isEqualTo(month);
            softly.assertThat(((Number) response.get("feedCount")).longValue()).isEqualTo(2L);
            softly.assertThat(((Number) response.get("likeCount")).longValue()).isEqualTo(1L);
            softly.assertThat(((Number) response.get("commentCount")).longValue()).isEqualTo(1L);
            softly.assertThat(((Number) response.get("score")).longValue()).isEqualTo(28L);
            softly.assertThat(((Number) response.get("rank")).intValue()).isEqualTo(1);
        });
    }

    @DisplayName("동아리 이달의 현황 조회 - rank 검증: 동아리A(피드2개) > 동아리B(피드1개)")
    @Test
    void getFeedStatus_rankVerification() {
        // given
        User userA = userRepository.save(UserFixture.createClubUser("clubA", passwordEncoder.encode("1234")));
        Club clubA = clubRepository.save(ClubFixture.createClub(userA));
        feedRepository.save(FeedFixture.createImageFeed(clubA, "피드A-1"));
        feedRepository.save(FeedFixture.createImageFeed(clubA, "피드A-2"));

        User userB = userRepository.save(UserFixture.createClubUser("clubB", passwordEncoder.encode("1234")));
        Club clubB = clubRepository.save(ClubFixture.createClub(userB));
        feedRepository.save(FeedFixture.createImageFeed(clubB, "피드B-1"));

        String tokenB = signIn("clubB", "1234");

        // when & then — 동아리B는 rank=2
        Map<?, ?> response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenB)
                .queryParam("year", year)
                .queryParam("month", month)
                .when()
                .get("/server/central/feeds/status")
                .then()
                .statusCode(200)
                .extract()
                .as(Map.class);

        assertSoftly(softly -> {
            softly.assertThat(((Number) response.get("rank")).intValue()).isEqualTo(2);
            softly.assertThat(((Number) response.get("feedCount")).longValue()).isEqualTo(1L);
            softly.assertThat(((Number) response.get("score")).longValue()).isEqualTo(10L);
        });
    }

    @DisplayName("동아리 이달의 현황 조회 - 동점 rank 검증: 동아리A·B 동점이면 둘 다 rank=1")
    @Test
    void getFeedStatus_tieRank() {
        // given
        User userA = userRepository.save(UserFixture.createClubUser("clubA", passwordEncoder.encode("1234")));
        Club clubA = clubRepository.save(ClubFixture.createClub(userA));
        feedRepository.save(FeedFixture.createImageFeed(clubA, "피드A"));

        User userB = userRepository.save(UserFixture.createClubUser("clubB", passwordEncoder.encode("1234")));
        Club clubB = clubRepository.save(ClubFixture.createClub(userB));
        feedRepository.save(FeedFixture.createImageFeed(clubB, "피드B"));

        String tokenB = signIn("clubB", "1234");

        // when & then — 동점이므로 둘 다 rank=1
        Map<?, ?> response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenB)
                .queryParam("year", year)
                .queryParam("month", month)
                .when()
                .get("/server/central/feeds/status")
                .then()
                .statusCode(200)
                .extract()
                .as(Map.class);

        assertSoftly(softly -> {
            softly.assertThat(((Number) response.get("rank")).intValue()).isEqualTo(1);
            softly.assertThat(((Number) response.get("score")).longValue()).isEqualTo(10L);
        });
    }

    @DisplayName("동아리 이달의 현황 조회 - 피드 없음: 모든 필드 0, rank=0")
    @Test
    void getFeedStatus_noFeeds() {
        // given
        User clubUser = userRepository.save(UserFixture.createClubUser(passwordEncoder.encode("1234")));
        clubRepository.save(ClubFixture.createClub(clubUser));

        String token = signIn("club123", "1234");

        // when & then — 피드가 없는 연/월 조회
        Map<?, ?> response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .queryParam("year", 2000)
                .queryParam("month", 1)
                .when()
                .get("/server/central/feeds/status")
                .then()
                .statusCode(200)
                .extract()
                .as(Map.class);

        assertSoftly(softly -> {
            softly.assertThat(((Number) response.get("rank")).intValue()).isEqualTo(0);
            softly.assertThat(((Number) response.get("feedCount")).longValue()).isEqualTo(0L);
            softly.assertThat(((Number) response.get("viewCount")).longValue()).isEqualTo(0L);
            softly.assertThat(((Number) response.get("likeCount")).longValue()).isEqualTo(0L);
            softly.assertThat(((Number) response.get("commentCount")).longValue()).isEqualTo(0L);
            softly.assertThat(((Number) response.get("score")).longValue()).isEqualTo(0L);
        });
    }

    @DisplayName("동아리 이달의 현황 조회 - 미인증 접근 시 401")
    @Test
    void getFeedStatus_unauthorized() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("year", year)
                .queryParam("month", month)
                .when()
                .get("/server/central/feeds/status")
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
