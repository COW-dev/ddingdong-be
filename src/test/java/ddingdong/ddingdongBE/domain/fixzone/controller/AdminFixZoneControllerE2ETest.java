package ddingdong.ddingdongBE.domain.fixzone.controller;

import static io.restassured.RestAssured.given;

import ddingdong.ddingdongBE.auth.controller.dto.request.SignInRequest;
import ddingdong.ddingdongBE.auth.controller.dto.response.SignInResponse;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FixZoneFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.NonTxTestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixZoneCommentRequest;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixZoneCommentRepository;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixZoneRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
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
class AdminFixZoneControllerE2ETest extends NonTxTestContainerSupport {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FixZoneRepository fixZoneRepository;

    @Autowired
    private FixZoneCommentRepository fixZoneCommentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User admin;
    private Club club;
    private FixZone fixZone;
    private String adminToken;

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void setUp() {
        RestAssured.port = port;

        // 어드민 사용자 생성 (비밀번호 인코딩)
        admin = UserFixture.createAdminUser(passwordEncoder.encode("1234"));
        admin = userRepository.save(admin);
        User clubUser = UserFixture.createClubUser(passwordEncoder.encode("1234"));
        clubUser = userRepository.save(clubUser);

        club = clubRepository.save(ClubFixture.createClub(clubUser));
        fixZone = fixZoneRepository.save(FixZoneFixture.createFixZone(club));

        // 어드민 로그인하여 토큰 획득
        adminToken = getAuthToken(admin.getAuthId(), "1234");
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

    @DisplayName("어드민이 픽스존 댓글을 생성한다")
    @Test
    void createFixZoneComment() {
        // given
        CreateFixZoneCommentRequest request = new CreateFixZoneCommentRequest("새로운 댓글 내용");

        // when & then
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(request)
                .when()
                .post("/server/admin/fix-zones/{fixZoneId}/comments", fixZone.getId())
                .then()
                .statusCode(201);

        // 댓글이 실제로 생성되었는지 확인
        long commentCount = fixZoneCommentRepository.count();
        Assertions.assertThat(commentCount).isGreaterThan(0);
    }

    @DisplayName("존재하지 않는 픽스존에 댓글 생성 시 404 에러가 발생한다")
    @Test
    void createFixZoneCommentWithNonExistentFixZone() {
        // given
        CreateFixZoneCommentRequest request = new CreateFixZoneCommentRequest("댓글 내용");
        Long nonExistentFixZoneId = 999L;

        // when & then
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(request)
                .when()
                .post("/server/admin/fix-zones/{fixZoneId}/comments", nonExistentFixZoneId)
                .then()
                .statusCode(404);
    }

    @DisplayName("인증되지 않은 사용자의 댓글 생성 요청은 401 에러가 발생한다")
    @Test
    void createFixZoneCommentWithoutAuthentication() {
        // given
        CreateFixZoneCommentRequest request = new CreateFixZoneCommentRequest("댓글 내용");

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/server/admin/fix-zones/{fixZoneId}/comments", fixZone.getId())
                .then()
                .statusCode(401);
    }

    @DisplayName("잘못된 토큰으로 댓글 생성 요청 시 401 에러가 발생한다")
    @Test
    void createFixZoneCommentWithInvalidToken() {
        // given
        CreateFixZoneCommentRequest request = new CreateFixZoneCommentRequest("댓글 내용");

        // when & then
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer invalid-token")
                .body(request)
                .when()
                .post("/server/admin/fix-zones/{fixZoneId}/comments", fixZone.getId())
                .then()
                .statusCode(401);
    }
}
