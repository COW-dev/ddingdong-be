package ddingdong.ddingdongBE.domain.form.service;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.domain.formapplication.service.FacadeCentralFormApplicationService;
import ddingdong.ddingdongBE.domain.formapplication.service.FormApplicationService;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.UpdateFormApplicationStatusCommand;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationQuery;
import ddingdong.ddingdongBE.domain.user.entity.Role;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FacadeCentralFormApplicationServiceImplTest extends TestContainerSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private FormApplicationService formApplicationService;

    @Autowired
    private FacadeCentralFormApplicationService facadeCentralFormApplicationService;

    private static final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("동아리는 지원자 응답을 상세조회 할 수 있다.")
    @Test
    void getFormApplication() {
        // given
        User user = fixture.giveMeBuilder(User.class)
                .set("id", 1L)
                .set("Role", Role.CLUB)
                .set("deletedAt", null)
                .sample();
        User savedUser = userRepository.saveAndFlush(user);
        Club club = fixture.giveMeBuilder(Club.class)
                .set("id", 1L)
                .set("user", savedUser)
                .set("score", null)
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sample();
        Club savedClub = clubRepository.saveAndFlush(club);
        Form form = fixture.giveMeBuilder(Form.class)
                .set("id", 1L)
                .set("title", "제목1")
                .set("club", savedClub)
                .sample();
        Form savedForm = formRepository.saveAndFlush(form);
        FormApplication formApplication = FormApplication.builder()
                .name("지원자1")
                .studentNumber("60201115")
                .department("융합소프트웨어학부")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm)
                .build();
        FormApplication savedApplication = formApplicationService.create(formApplication);
        // when
        FormApplicationQuery formApplicationQuery = facadeCentralFormApplicationService.getFormApplication(1L, savedApplication.getId(), savedUser);
        // then
        assertThat(formApplicationQuery.name()).isEqualTo("지원자1");
    }

    @DisplayName("동아리는 지원자의 상태를 수정할 수 있다.")
    @Test
    void updateFormApplicationStatus() {
        // given
        User user = fixture.giveMeBuilder(User.class)
                .set("id", 1L)
                .set("Role", Role.CLUB)
                .set("deletedAt", null)
                .sample();
        User savedUser = userRepository.saveAndFlush(user);
        Club club = fixture.giveMeBuilder(Club.class)
                .set("id", 1L)
                .set("user", savedUser)
                .set("score", null)
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sample();
        Club savedClub = clubRepository.saveAndFlush(club);
        Form form = fixture.giveMeBuilder(Form.class)
                .set("id", 1L)
                .set("title", "제목1")
                .set("club", savedClub)
                .sample();
        Form savedForm = formRepository.saveAndFlush(form);
        FormApplication formApplication = FormApplication.builder()
                .name("지원자1")
                .studentNumber("60201115")
                .department("융합소프트웨어학부")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm)
                .build();
        FormApplication savedApplication = formApplicationService.create(formApplication);
        UpdateFormApplicationStatusCommand command = fixture.giveMeBuilder(UpdateFormApplicationStatusCommand.class)
                .set("formId", savedForm.getId())
                .set("applicationId", savedApplication.getId())
                .set("status", FormApplicationStatus.FIRST_PASS)
                .set("user", savedUser)
                .sample();
        // when
        facadeCentralFormApplicationService.updateStatus(command);
        // then
        assertThat(savedApplication.getName()).isEqualTo("지원자1");
        assertThat(savedApplication.getStudentNumber()).isEqualTo("60201115");
        assertThat(formApplication.getStatus()).isEqualTo(FormApplicationStatus.FIRST_PASS);
    }

}