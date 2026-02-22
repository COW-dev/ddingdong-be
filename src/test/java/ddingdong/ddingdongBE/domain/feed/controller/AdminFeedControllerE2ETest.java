package ddingdong.ddingdongBE.domain.feed.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.auth.controller.dto.request.SignInRequest;
import ddingdong.ddingdongBE.auth.controller.dto.response.SignInResponse;
import ddingdong.ddingdongBE.common.fixture.FeedMonthlyRankingFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.NonTxTestContainerSupport;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.AdminFeedRankingWinnerResponse;
import ddingdong.ddingdongBE.domain.feed.repository.FeedMonthlyRankingRepository;
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
    private FeedMonthlyRankingRepository feedMonthlyRankingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminToken;

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void setUp() {
        RestAssured.port = port;

        User admin = userRepository.save(
                UserFixture.createAdminUser(passwordEncoder.encode("1234")));

        feedMonthlyRankingRepository.saveAll(List.of(
                FeedMonthlyRankingFixture.createWinner(1L, "1월 우승 동아리", 2025, 1),
                FeedMonthlyRankingFixture.createWinner(2L, "2월 우승 동아리", 2025, 2),
                FeedMonthlyRankingFixture.createWinner(3L, "3월 우승 동아리", 2025, 3)
        ));

        adminToken = getAuthToken(admin.getAuthId(), "1234");
    }

    @DisplayName("총동연 월별 1위 동아리 목록 조회 API - 성공")
    @Test
    void getMonthlyWinners_success() {
        List<AdminFeedRankingWinnerResponse> response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("year", 2025)
                .when()
                .get("/server/admin/feeds/ranking/last")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", AdminFeedRankingWinnerResponse.class);

        assertThat(response).hasSize(3);
        assertThat(response.get(0).clubName()).isEqualTo("1월 우승 동아리");
        assertThat(response.get(0).targetMonth()).isEqualTo(1);
        assertThat(response.get(2).clubName()).isEqualTo("3월 우승 동아리");
        assertThat(response.get(2).targetMonth()).isEqualTo(3);
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
