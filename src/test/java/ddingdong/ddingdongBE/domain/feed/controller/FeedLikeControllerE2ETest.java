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
import ddingdong.ddingdongBE.domain.feed.repository.FeedLikeRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
class FeedLikeControllerE2ETest extends NonTxTestContainerSupport {

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

    private Feed feed;
    private String userToken;

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void setUp() {
        RestAssured.port = port;

        User user = userRepository.save(UserFixture.createGeneralUser(passwordEncoder.encode("1234")));
        Club club = clubRepository.save(ClubFixture.createClub());
        feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));

        userToken = getAuthToken(user.getAuthId(), "1234");
    }

    private String getAuthToken(String authId, String password) {
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

    @DisplayName("인증된 사용자가 피드 좋아요를 생성한다.")
    @Test
    void createLike_success() {
        // when & then
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userToken)
                .when()
                .post("/server/feeds/{feedId}/likes", feed.getId())
                .then()
                .statusCode(201);

        long count = feedLikeRepository.countByFeedId(feed.getId());
        assertThat(count).isEqualTo(1L);
    }

    @DisplayName("인증되지 않은 사용자의 좋아요 요청은 401을 반환한다.")
    @Test
    void createLike_fail_unauthorized() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/server/feeds/{feedId}/likes", feed.getId())
                .then()
                .statusCode(401);
    }

    @DisplayName("존재하지 않는 피드에 좋아요하면 404를 반환한다.")
    @Test
    void createLike_fail_feedNotFound() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userToken)
                .when()
                .post("/server/feeds/{feedId}/likes", 9999L)
                .then()
                .statusCode(404);
    }

}
