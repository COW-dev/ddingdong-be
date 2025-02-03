package ddingdong.ddingdongBE.domain.form.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.repository.FormFieldRepository;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.formapplicaion.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplicaion.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplicaion.repository.FormAnswerRepository;
import ddingdong.ddingdongBE.domain.formapplicaion.repository.FormApplicationRepository;
import ddingdong.ddingdongBE.domain.formapplicaion.service.FacadeUserFormService;
import ddingdong.ddingdongBE.domain.formapplicaion.service.dto.command.CreateFormApplicationCommand;
import ddingdong.ddingdongBE.domain.formapplicaion.service.dto.command.CreateFormApplicationCommand.CreateFormAnswerCommand;
import ddingdong.ddingdongBE.domain.user.entity.Role;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Autowired
    private FormFieldRepository formFieldRepository;

    @Autowired
    private EntityManager entityManager;

    private static final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("사용자는 동아리에 지원할 수 있다.")
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
        FormField formField = FormField.builder()
                .form(savedForm)
                .fieldType(FieldType.CHECK_BOX)
                .fieldOrder(1)
                .section("서버")
                .required(true)
                .question("질문")
                .build();
        FormField savedFormField = formFieldRepository.save(formField);
        CreateFormApplicationCommand createFormApplicationCommand = fixtureMonkey.giveMeBuilder(CreateFormApplicationCommand.class)
                .set("formId", savedForm.getId())
                .set("formAnswerCommands", List.of(new CreateFormAnswerCommand(savedFormField.getId(), List.of("답변"))))
                .sample();
        // when
        facadeUserFormService.createFormApplication(createFormApplicationCommand);
        // then
        List<FormApplication> formApplications = formApplicationRepository.findAll();
        List<FormAnswer> formAnswers  = formAnswerRepository.findAll();

        assertThat(formApplications).isNotEmpty();
        assertThat(formApplications.get(0).getForm().getId()).isEqualTo(savedForm.getId());
        assertThat(formAnswers).isNotEmpty();
        assertThat(formAnswers.get(0).getValue()).isEqualTo(List.of("답변"));
    }
}
