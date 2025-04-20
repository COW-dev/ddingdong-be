package ddingdong.ddingdongBE.domain.form.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.repository.FormFieldRepository;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormSectionQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.UserFormQuery;
import ddingdong.ddingdongBE.domain.user.entity.Role;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeUserFormServiceImplTest extends TestContainerSupport {

  @Autowired
  private FacadeUserFormService facadeUserFormService;

  @Autowired
  private FormRepository formRepository;

  @Autowired
  private ClubRepository clubRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FormFieldRepository formFieldRepository;

  private User savedUser;
  private Club savedClub;
  private Form savedForm;
  private List<String> savedSections;

  @BeforeEach
  void setUp() {
    User user = User.builder()
            .id(1L)
            .role(Role.CLUB)
            .build();
    savedUser = userRepository.save(user);

    Club club = Club.builder()
            .id(1L)
            .user(savedUser)
            .score(null)
            .clubMembers(null)
            .deletedAt(null)
            .build();
    savedClub = clubRepository.save(club);

    savedSections = Arrays.asList("section1", "section2");

    Form form = Form.builder()
            .title("띵동 폼")
            .description("저희 동아리는 띵동입니다.")
            .hasInterview(false)
            .club(savedClub)
            .sections(savedSections)
            .endDate(LocalDate.now().plusMonths(1))
            .startDate(LocalDate.now())
            .build();
    savedForm = formRepository.save(form);
  }

  @DisplayName("유저는 섹션 목록을 조회할 수 있다.")
  @Test
  void getFormSection() {
    // given

    // when
    FormSectionQuery sectionQuery = facadeUserFormService.getFormSection(savedForm.getId());

    // then
    assertThat(sectionQuery.sections()).isEqualTo(savedSections);
  }

  @DisplayName("유저는 폼지를 상세조회 할 수 있다.")
  @Test
  void getForm() {
    // given
    FormField savedFormField1 = createFormField("질문1", 1, savedSections.get(0), savedForm);
    FormField savedFormField2 = createFormField("질문2", 2, savedSections.get(1), savedForm);

    String selectedSection = savedSections.get(0);

    // when
    UserFormQuery userFormQuery = facadeUserFormService.getUserForm(savedForm.getId(), selectedSection);

    // then
    assertThat(userFormQuery.formFields().get(0).id()).isEqualTo(savedFormField1.getId());
  }

  private FormField createFormField(String question, int order, String section, Form form) {
    FormField formField = FormField.builder()
            .question(question)
            .required(true)
            .fieldOrder(order)
            .section(section)
            .fieldType(FieldType.TEXT)
            .form(form)
            .build();
    return formFieldRepository.save(formField);
  }
}
