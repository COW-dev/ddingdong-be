package ddingdong.ddingdongBE.domain.feed.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.support.NonTxTestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeedLikeControllerE2ETest extends NonTxTestContainerSupport {

    @LocalServerPort
    private int port;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FeedRepository feedRepository;

    private Feed feed;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        Club club = clubRepository.save(ClubFixture.createClub());
        feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));
    }

    @DisplayName("좋아요 5회를 한 번에 보내면 카운터가 5 증가한다")
    @Test
    void createLike_success() {
        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("count", 5))
                .when()
                .patch("/server/feeds/{feedId}/likes", feed.getId())
                .then()
                .statusCode(204);

        Feed found = feedRepository.findById(feed.getId()).orElseThrow();
        assertThat(found.getLikeCount()).isEqualTo(5L);
    }

    @DisplayName("같은 피드에 여러 번 요청하면 좋아요가 누적된다")
    @Test
    void createLike_accumulates() {
        // when
        for (int i = 0; i < 3; i++) {
            given()
                    .contentType(ContentType.JSON)
                    .body(Map.of("count", 10))
                    .when()
                    .patch("/server/feeds/{feedId}/likes", feed.getId())
                    .then()
                    .statusCode(204);
        }

        // then
        Feed found = feedRepository.findById(feed.getId()).orElseThrow();
        assertThat(found.getLikeCount()).isEqualTo(30L);
    }

    @DisplayName("좋아요 횟수가 100을 초과하면 400을 반환한다")
    @Test
    void createLike_exceedsMaxCount() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("count", 101))
                .when()
                .patch("/server/feeds/{feedId}/likes", feed.getId())
                .then()
                .statusCode(400);
    }

    @DisplayName("좋아요 횟수가 0 이하이면 400을 반환한다")
    @Test
    void createLike_zeroCount() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("count", 0))
                .when()
                .patch("/server/feeds/{feedId}/likes", feed.getId())
                .then()
                .statusCode(400);
    }

    @DisplayName("존재하지 않는 피드에 좋아요하면 404를 반환한다")
    @Test
    void createLike_nonExistentFeed() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("count", 1))
                .when()
                .patch("/server/feeds/{feedId}/likes", 999999L)
                .then()
                .statusCode(404);
    }
}
