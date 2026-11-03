package ddingdong.ddingdongBE.domain.activityreport.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import ddingdong.ddingdongBE.auth.controller.dto.request.SignInRequest;
import ddingdong.ddingdongBE.auth.controller.dto.response.SignInResponse;
import ddingdong.ddingdongBE.common.fixture.ActivityReportFixture;
import ddingdong.ddingdongBE.common.fixture.ActivityReportTermInfoFixture;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FileMetaDataFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.NonTxTestContainerSupport;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportRepository;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportTermInfoRepository;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminActivityReportControllerE2ETest extends NonTxTestContainerSupport {

    private static final String CREATE_TERM_PATH = "/server/admin/activity-reports/term";

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityReportTermInfoRepository activityReportTermInfoRepository;
    @Autowired
    private ActivityReportRepository activityReportRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        User admin = userRepository.save(UserFixture.createAdminUser(passwordEncoder.encode("1234")));
        adminToken = getAuthToken(admin.getAuthId(), "1234");
    }

    private String getAuthToken(String authId, String password) {
        return given()
                .contentType(ContentType.JSON)
                .body(new SignInRequest(authId, password))
                .when()
                .post("/server/auth/sign-in")
                .then()
                .statusCode(200)
                .extract()
                .as(SignInResponse.class)
                .getToken();
    }

    private Map<String, Object> createTermRequest(String startDate, int totalTermCount) {
        Map<String, Object> request = new HashMap<>();
        request.put("startDate", startDate);
        request.put("totalTermCount", totalTermCount);
        return request;
    }

    @DisplayName("어드민이 시작일과 회차 수를 입력하면 회차가 생성된다")
    @Test
    void createTerms() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(createTermRequest("2027-03-02", 7))
                .when()
                .post(CREATE_TERM_PATH)
                .then()
                .statusCode(201);

        assertThat(activityReportTermInfoRepository.count()).isEqualTo(7);
    }

    @DisplayName("회차 수가 1보다 작으면 회차를 생성할 수 없다")
    @Test
    void cannotCreateTermsWithZeroCount() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(createTermRequest("2027-03-02", 0))
                .when()
                .post(CREATE_TERM_PATH)
                .then()
                .statusCode(400)
                .body("message", equalTo("totalTermCount는 1 이상이어야 합니다."));
    }

    @DisplayName("시작일이 없으면 회차를 생성할 수 없다")
    @Test
    void cannotCreateTermsWithoutStartDate() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(createTermRequest(null, 7))
                .when()
                .post(CREATE_TERM_PATH)
                .then()
                .statusCode(400)
                .body("message", equalTo("startDate는 null이 될 수 없습니다."));
    }

    @DisplayName("기존 회차와 기간이 겹치면 회차를 생성할 수 없다")
    @Test
    void cannotCreateTermsOverlappingExistingTerm() {
        activityReportTermInfoRepository.save(
                ActivityReportTermInfoFixture.create(1, LocalDate.of(2027, 3, 2), LocalDate.of(2027, 3, 15)));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .body(createTermRequest("2027-03-02", 7))
                .when()
                .post(CREATE_TERM_PATH)
                .then()
                .statusCode(400)
                .body("message", equalTo("회차 시작일은 기존 회차 마감일 이후여야 합니다."));

        assertThat(activityReportTermInfoRepository.count()).isEqualTo(1);
    }

    @DisplayName("제출된 활동보고서가 없는 올해 회차는 어드민이 삭제할 수 있다")
    @Test
    void deleteTermWithoutSubmittedReports() {
        // given
        LocalDate startOfThisYear = Year.now().atDay(1);
        activityReportTermInfoRepository.save(
                ActivityReportTermInfoFixture.create(0, startOfThisYear, startOfThisYear.plusDays(13)));

        // when
        given()
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("term", 0)
                .when()
                .delete(CREATE_TERM_PATH)
                .then()
                .statusCode(204);

        // then
        assertThat(activityReportTermInfoRepository.count()).isZero();
    }

    @DisplayName("제출된 활동보고서가 하나라도 있는 회차는 삭제할 수 없다")
    @Test
    void cannotDeleteTermWithSubmittedReports() {
        // given
        LocalDate startOfThisYear = Year.now().atDay(1);
        activityReportTermInfoRepository.save(
                ActivityReportTermInfoFixture.create(0, startOfThisYear, startOfThisYear.plusDays(13)));
        Club club = clubRepository.save(ClubFixture.createClub(userRepository.save(UserFixture.createClubUser())));
        activityReportRepository.save(ActivityReportFixture.create(club, 0));

        // when & then
        given()
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("term", 0)
                .when()
                .delete(CREATE_TERM_PATH)
                .then()
                .statusCode(400)
                .body("message", equalTo("제출된 활동보고서가 있어 회차를 삭제할 수 없습니다."));

        assertThat(activityReportTermInfoRepository.count()).isEqualTo(1);
    }

    @DisplayName("올해 존재하지 않는 회차는 삭제할 수 없다")
    @Test
    void cannotDeleteNonExistentTerm() {
        // given
        activityReportTermInfoRepository.save(ActivityReportTermInfoFixture.create(
                1, LocalDate.of(2020, 3, 2), LocalDate.of(2020, 3, 15)));

        // when & then
        given()
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("term", 1)
                .when()
                .delete(CREATE_TERM_PATH)
                .then()
                .statusCode(404);

        assertThat(activityReportTermInfoRepository.count()).isEqualTo(1);
    }

    @DisplayName("어드민은 마감이 지난 회차의 동아리 활동보고서도 수정할 수 있다")
    @Test
    void updateClubActivityReportAfterTermClosed() {
        // given
        LocalDate startOfThisYear = Year.now().atDay(1);
        activityReportTermInfoRepository.save(
                ActivityReportTermInfoFixture.create(1, startOfThisYear, startOfThisYear.plusDays(13)));
        Club club = clubRepository.save(ClubFixture.createClub(userRepository.save(UserFixture.createClubUser())));
        ActivityReport activityReport = activityReportRepository.save(ActivityReportFixture.create(club, 1));

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("content", "총동연이 수정한 내용입니다");
        updateRequest.put("place", "S1350");
        updateRequest.put("startDate", "2026-03-20 10:00");
        updateRequest.put("endDate", "2026-03-20 12:00");
        updateRequest.put("participants", List.of());

        // when
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("term", 1)
                .body(Map.of("activityReportRequests", List.of(updateRequest)))
                .when()
                .patch("/server/admin/activity-reports/clubs/{clubId}", club.getId())
                .then()
                .statusCode(204);

        // then
        assertThat(activityReportRepository.findById(activityReport.getId()))
                .hasValueSatisfying(updatedReport -> assertThat(updatedReport.getContent())
                        .isEqualTo("총동연이 수정한 내용입니다"));
    }

    @DisplayName("해당 회차에 동아리 활동보고서가 없으면 어드민도 수정할 수 없다")
    @Test
    void cannotUpdateNonExistentClubActivityReport() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub(userRepository.save(UserFixture.createClubUser())));

        // when & then
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("term", 1)
                .body(Map.of("activityReportRequests", List.of()))
                .when()
                .patch("/server/admin/activity-reports/clubs/{clubId}", club.getId())
                .then()
                .statusCode(404);
    }

    @DisplayName("수정 요청한 보고서 수가 제출된 보고서 수보다 많으면 아무것도 수정되지 않는다")
    @Test
    void cannotUpdateMoreReportsThanSubmitted() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub(userRepository.save(UserFixture.createClubUser())));
        ActivityReport activityReport = activityReportRepository.save(ActivityReportFixture.create(club, 1));
        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("content", "총동연이 수정한 내용입니다");
        updateRequest.put("startDate", "2026-03-20 10:00");
        updateRequest.put("endDate", "2026-03-20 12:00");
        updateRequest.put("participants", List.of());

        // when & then
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("term", 1)
                .body(Map.of("activityReportRequests", List.of(updateRequest, updateRequest)))
                .when()
                .patch("/server/admin/activity-reports/clubs/{clubId}", club.getId())
                .then()
                .statusCode(400)
                .body("message", equalTo("수정할 활동보고서 수가 제출된 활동보고서 수보다 많습니다."));

        assertThat(activityReportRepository.findById(activityReport.getId()))
                .hasValueSatisfying(unchangedReport -> assertThat(unchangedReport.getContent())
                        .isEqualTo("세션을 진행하였습니다"));
    }

    @DisplayName("어드민은 0회차처럼 잘못 제출된 동아리 활동보고서를 삭제할 수 있고, 연결된 이미지도 삭제 처리된다")
    @Test
    void deleteClubActivityReport() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub(userRepository.save(UserFixture.createClubUser())));
        ActivityReport activityReport = activityReportRepository.save(ActivityReportFixture.create(club, 0));
        UUID imageId = UUID.randomUUID();
        fileMetaDataRepository.save(
                FileMetaDataFixture.create(imageId, activityReport.getId(), DomainType.ACTIVITY_REPORT_IMAGE));

        // when
        given()
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("term", 0)
                .when()
                .delete("/server/admin/activity-reports/clubs/{clubId}", club.getId())
                .then()
                .statusCode(204);

        // then
        assertThat(activityReportRepository.count()).isZero();
        assertThat(fileMetaDataRepository.findById(imageId))
                .hasValueSatisfying(fileMetaData -> assertThat(fileMetaData.getFileStatus())
                        .isEqualTo(FileStatus.DELETED));
    }

    @DisplayName("해당 회차에 동아리 활동보고서가 없으면 삭제할 수 없다")
    @Test
    void cannotDeleteNonExistentClubActivityReport() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub(userRepository.save(UserFixture.createClubUser())));

        // when & then
        given()
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("term", 1)
                .when()
                .delete("/server/admin/activity-reports/clubs/{clubId}", club.getId())
                .then()
                .statusCode(404);
    }
}
