package ddingdong.ddingdongBE.domain.feed.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.support.NonTxTestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedLikeRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.service.FeedLikeCacheService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeedLikeControllerE2ETest extends NonTxTestContainerSupport {

    private static final String VALID_UUID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String OTHER_UUID = "660f9511-f3ac-42e5-b827-557766551111";
    private static final String INVALID_UUID = "not-a-valid-uuid";

    @LocalServerPort
    private int port;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedLikeRepository feedLikeRepository;

    @Autowired
    private FeedLikeCacheService feedLikeCacheService;

    private Feed feed;

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void setUp() {
        RestAssured.port = port;
        feedLikeCacheService.clearAll();

        Club club = clubRepository.save(ClubFixture.createClub());
        feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));
    }

    @DisplayName("비회원이 X-Anonymous-UUID 헤더로 피드 좋아요를 생성한다.")
    @Test
    void createLike_success() {
        // when & then
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", VALID_UUID)
                .when()
                .post("/server/feeds/{feedId}/likes", feed.getId())
                .then()
                .statusCode(201);

        long count = feedLikeRepository.countByFeedId(feed.getId());
        assertThat(count).isEqualTo(1L);
    }

    @DisplayName("유효하지 않은 UUID로 좋아요 요청하면 400을 반환한다.")
    @Test
    void createLike_fail_invalidUuid() {
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", INVALID_UUID)
                .when()
                .post("/server/feeds/{feedId}/likes", feed.getId())
                .then()
                .statusCode(400);
    }

    @DisplayName("존재하지 않는 피드에 좋아요하면 404를 반환한다.")
    @Test
    void createLike_fail_feedNotFound() {
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", VALID_UUID)
                .when()
                .post("/server/feeds/{feedId}/likes", 9999L)
                .then()
                .statusCode(404);
    }

    @DisplayName("이미 좋아요한 피드에 다시 좋아요하면 409를 반환한다.")
    @Test
    void createLike_fail_duplicate() {
        // given: 첫 번째 좋아요
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", OTHER_UUID)
                .when()
                .post("/server/feeds/{feedId}/likes", feed.getId())
                .then()
                .statusCode(201);

        // when & then: 두 번째 좋아요
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", OTHER_UUID)
                .when()
                .post("/server/feeds/{feedId}/likes", feed.getId())
                .then()
                .statusCode(409);
    }

    @DisplayName("비회원이 X-Anonymous-UUID 헤더로 피드 좋아요를 취소한다.")
    @Test
    void deleteLike_success() {
        // given: 좋아요 생성
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", VALID_UUID)
                .when()
                .post("/server/feeds/{feedId}/likes", feed.getId())
                .then()
                .statusCode(201);

        // when & then
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", VALID_UUID)
                .when()
                .delete("/server/feeds/{feedId}/likes", feed.getId())
                .then()
                .statusCode(204);

        long count = feedLikeRepository.countByFeedId(feed.getId());
        assertThat(count).isEqualTo(0L);
    }

    @DisplayName("좋아요 기록이 없는 피드를 취소하면 404를 반환한다.")
    @Test
    void deleteLike_fail_notFound() {
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", VALID_UUID)
                .when()
                .delete("/server/feeds/{feedId}/likes", feed.getId())
                .then()
                .statusCode(404);
    }

    @DisplayName("유효하지 않은 UUID로 좋아요 취소 요청하면 400을 반환한다.")
    @Test
    void deleteLike_fail_invalidUuid() {
        given()
                .contentType(ContentType.JSON)
                .header("X-Anonymous-UUID", INVALID_UUID)
                .when()
                .delete("/server/feeds/{feedId}/likes", feed.getId())
                .then()
                .statusCode(400);
    }
}
