package ddingdong.ddingdongBE.domain.form.service;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.formapplicaion.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplicaion.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplicaion.repository.FormAnswerRepository;
import ddingdong.ddingdongBE.domain.formapplicaion.repository.FormApplicationRepository;
import ddingdong.ddingdongBE.domain.formapplicaion.service.FacadeUserFormService;
import ddingdong.ddingdongBE.domain.formapplicaion.service.dto.CreateFormApplicationCommand;
import ddingdong.ddingdongBE.domain.user.entity.Role;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
class FacadeUserFormServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeUserFormService facadeUserFormService;

    @Autowired
    private FormApplicationRepository formApplicationRepository;

    @Autowired
    private FormAnswerRepository formAnswerRepository;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private UserRepository userRepository;

    private static final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("유저: 지원하기")
    @Test
    void createFormApplication() {
        // given
        User user = fixtureMonkey.giveMeBuilder(User.class)
                .set("id", 1L)
                .set("Role", Role.CLUB)
                .set("deletedAt", null)
                .sample();
        User savedUser = userRepository.save(user);
        Club club = fixtureMonkey.giveMeBuilder(Club.class)
                .set("id", 1L)
                .set("user", savedUser)
                .set("score", null)
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sample();
        clubRepository.save(club);
        Club savedClub = clubRepository.save(club);
        Form form = fixtureMonkey.giveMeBuilder(Form.class)
                .set("id", 1L)
                .set("title", "띵동 폼")
                .set("description", "저희 동아리는 띵동입니다.")
                .set("hasInterview", false)
                .set("club", savedClub)
                .sample();
        Form savedForm = formRepository.save(form);
        CreateFormApplicationCommand createFormApplicationCommand = fixtureMonkey.giveMeBuilder(CreateFormApplicationCommand.class)
                .set("form", savedForm)
                .sample();
        // when
        facadeUserFormService.createFormApplication(savedForm.getId(), createFormApplicationCommand);
        // then
        List<FormApplication> formApplications = formApplicationRepository.findAll();
        List<FormAnswer> formAnswers  = formAnswerRepository.findAll();

        assertThat(formApplications).isNotEmpty();
        assertThat(formAnswers).isNotEmpty();
    }

  
}