package ddingdong.ddingdongBE.domain.form.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.exception.AuthenticationException.NonHaveAuthority;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.repository.FormFieldRepository;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand.UpdateFormFieldCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormListQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormQuery;
import ddingdong.ddingdongBE.domain.user.entity.Role;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeCentralFormServiceImplTest extends TestContainerSupport {

    @Autowired
    private FormService formService;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private FacadeCentralFormService facadeCentralFormService;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FormFieldRepository formFieldRepository;

    private static final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("폼지와 폼지 질문을 생성할 수 있다.")
    @Test
    void createForm() {
        // given
        User user = User.builder()
                .role(Role.CLUB)
                .build();
        User savedUser = userRepository.save(user);
        Club club = Club.builder()
                .user(savedUser)
                .build();
        clubRepository.save(club);
        CreateFormCommand createFormCommand = fixtureMonkey.giveMeBuilder(CreateFormCommand.class)
                .set("user", savedUser)
                .sample();
        // when
        facadeCentralFormService.createForm(createFormCommand);
        // then
        List<Form> form = formRepository.findAll();
        List<FormField> formFields = formFieldRepository.findAll();

        assertThat(form).isNotEmpty();
        assertThat(formFields).isNotEmpty();
    }

    @DisplayName("폼지와 폼지 질문을 수정할 수 있다.")
    @Test
    void updateForm() {
        // given
        User user = User.builder()
                .role(Role.CLUB)
                .build();
        User savedUser = userRepository.save(user);
        Club club = Club.builder()
                .user(savedUser)
                .build();
        Club savedClub = clubRepository.save(club);
        Form form = fixtureMonkey.giveMeBuilder(Form.class)
                .set("club", savedClub)
                .sample();
        Form savedForm = formService.create(form);
        UpdateFormCommand updateFormCommand = fixtureMonkey.giveMeBuilder(UpdateFormCommand.class)
                .set("title", "수정된 제목")
                .set("description", "수정된 설명")
                .set("formId", savedForm.getId())
                .set("formFieldCommands", List.of(
                        fixtureMonkey.giveMeBuilder(UpdateFormFieldCommand.class)
                                .set("question", "수정된 질문")
                                .sample())
                )
                .sample();
        // when
        facadeCentralFormService.updateForm(updateFormCommand);
        // then
        Form found = formRepository.findById(savedForm.getId()).orElse(null);
        List<FormField> formFields = formFieldRepository.findAllByForm(found);
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("수정된 제목");
        assertThat(found.getDescription()).isEqualTo("수정된 설명");
        assertThat(formFields).isNotEmpty();
        assertThat(formFields.get(0).getQuestion()).isEqualTo("수정된 질문");

    }

    @DisplayName("폼지를 삭제할 수 있다. 폼지를 삭제하면, 하위 폼지 필드도 모두 삭제된다.")
    @Test
    void deleteForm() {
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
        Form form = fixtureMonkey.giveMeBuilder(Form.class)
                .set("club", club)
                .sample();
        Form savedForm = formService.create(form);
        // when
        facadeCentralFormService.deleteForm(savedForm.getId(), user);
        // then
        Form found = formRepository.findById(savedForm.getId()).orElse(null);
        List<FormField> formFields = formFieldRepository.findAllByForm(savedForm);
        assertThat(found).isNull();
        assertThat(formFields).isEmpty();
    }

    @DisplayName("Club은 자신의 폼지가 아닌 폼지를 삭제할 수 없다.")
    @Test
    void validateEqualsClub() {
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
        User user2 = fixtureMonkey.giveMeBuilder(User.class)
                .set("id", 2L)
                .set("Role", Role.CLUB)
                .set("deletedAt", null)
                .sample();
        User savedUser2 = userRepository.save(user2);
        Club club2 = fixtureMonkey.giveMeBuilder(Club.class)
                .set("id", 2L)
                .set("user", savedUser2)
                .set("score", null)
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sample();
        clubRepository.save(club2);

        Form form = fixtureMonkey.giveMeBuilder(Form.class)
                .set("club", club)
                .sample();
        Form savedForm = formService.create(form);
        // when //then
        assertThrows(NonHaveAuthority.class, () -> {
            facadeCentralFormService.deleteForm(savedForm.getId(), user2);
        });
    }

    @DisplayName("동아리는 자신의 폼지를 전부 조회할 수 있다.")
    @Test
    void getAllMyForm() {
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
        Form form = fixtureMonkey.giveMeBuilder(Form.class)
                .set("title", "제목1")
                .set("club", club)
                .sample();
        Form form2 = fixtureMonkey.giveMeBuilder(Form.class)
                .set("title", "제목2")
                .set("club", club)
                .sample();
        formService.create(form);
        formService.create(form2);

        // when
        List<FormListQuery> queries = facadeCentralFormService.getAllMyForm(savedUser);
        // then
        assertThat(queries).isNotEmpty();
        assertThat(queries.size()).isEqualTo(2);
        assertThat(queries.get(0).title()).isEqualTo("제목1");
        assertThat(queries.get(1).title()).isEqualTo("제목2");

    }

    @DisplayName("동아리는 폼지를 상세조회 할 수 있다.")
    @Test
    void getForm() {
      // given
        Form form = fixtureMonkey.giveMeBuilder(Form.class)
                .set("id", 1L)
                .set("title", "제목1")
                .set("club", null)
                .sample();
        Form form2 = fixtureMonkey.giveMeBuilder(Form.class)
                .set("id", 2L)
                .set("title", "제목2")
                .set("club", null)
                .sample();
        formService.create(form);
        formService.create(form2);
      // when
        FormQuery formQuery = facadeCentralFormService.getForm(1L);
        // then
        assertThat(formQuery.title()).isEqualTo("제목1");
    }
}
