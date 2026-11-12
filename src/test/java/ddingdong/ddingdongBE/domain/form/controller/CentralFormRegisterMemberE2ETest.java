package ddingdong.ddingdongBE.domain.form.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.auth.controller.dto.request.SignInRequest;
import ddingdong.ddingdongBE.auth.controller.dto.response.SignInResponse;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.ClubMemberFixture;
import ddingdong.ddingdongBE.common.fixture.FormFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.NonTxTestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.clubmember.repository.ClubMemberRepository;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CentralFormRegisterMemberE2ETest extends NonTxTestContainerSupport {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private FormApplicationRepository formApplicationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("등록 방식을 지정하지 않고 합격자를 연동하면 기존 동아리원이 유지된 채 합격자가 추가된다.")
    @Test
    void registerMembersWithoutModeKeepsExistingMembers() {
        // given
        User clubUser = userRepository.save(UserFixture.createClubUser(passwordEncoder.encode("1234")));
        Club club = ClubFixture.createClub(clubUser);
        ClubMember originMember = ClubMemberFixture.createClubMember(club);
        club.addClubMember(originMember);
        Club savedClub = clubRepository.save(club);

        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));
        FormApplication formApplication = formApplicationRepository.save(
                FormFixture.createFormApplicationFinalPass(savedForm));

        String token = signIn("club123", "1234");

        // when
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/server/central/my/forms/{formId}/members/register-applicants", savedForm.getId())
                .then()
                .statusCode(201);

        // then
        List<ClubMember> remainingMembers = clubMemberRepository.findByClubId(savedClub.getId());
        assertThat(remainingMembers)
                .extracting(ClubMember::getName)
                .containsExactlyInAnyOrder(originMember.getName(), formApplication.getName());
    }

    @DisplayName("교체 방식으로 합격자를 연동하면 기존 동아리원은 삭제되고 합격자만 남는다.")
    @Test
    void registerMembersWithReplaceMode() {
        // given
        User clubUser = userRepository.save(UserFixture.createClubUser(passwordEncoder.encode("1234")));
        Club club = ClubFixture.createClub(clubUser);
        club.addClubMember(ClubMemberFixture.createClubMember(club));
        Club savedClub = clubRepository.save(club);

        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));
        FormApplication formApplication = formApplicationRepository.save(
                FormFixture.createFormApplicationFinalPass(savedForm));

        String token = signIn("club123", "1234");

        // when
        given()
                .header("Authorization", "Bearer " + token)
                .queryParam("mode", "REPLACE")
                .when()
                .post("/server/central/my/forms/{formId}/members/register-applicants", savedForm.getId())
                .then()
                .statusCode(201);

        // then
        List<ClubMember> remainingMembers = clubMemberRepository.findByClubId(savedClub.getId());
        assertThat(remainingMembers)
                .extracting(ClubMember::getName)
                .containsExactly(formApplication.getName());
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
