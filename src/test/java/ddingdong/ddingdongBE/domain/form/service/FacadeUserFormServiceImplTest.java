//package ddingdong.ddingdongBE.domain.form.service;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//
//import com.navercorp.fixturemonkey.FixtureMonkey;
//import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
//import ddingdong.ddingdongBE.common.support.TestContainerSupport;
//import ddingdong.ddingdongBE.domain.club.entity.Club;
//import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
//import ddingdong.ddingdongBE.domain.form.entity.Form;
//import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
//import ddingdong.ddingdongBE.domain.form.service.dto.query.FormSectionQuery;
//import ddingdong.ddingdongBE.domain.user.entity.Role;
//import ddingdong.ddingdongBE.domain.user.entity.User;
//import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class FacadeUserFormServiceImplTest extends TestContainerSupport {
//
//  @Autowired
//  private FacadeUserFormService facadeUserFormService;
//
//  @Autowired
//  private FormRepository formRepository;
//
//  @Autowired
//  private ClubRepository clubRepository;
//
//  @Autowired
//  private UserRepository userRepository;
//
//  private static final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();
//
//  @DisplayName("유저는 섹션 목록을 조회할 수 있다.")
//  @Test
//  void getFormSection() {
//    // given
//    User user = fixtureMonkey.giveMeBuilder(User.class)
//        .set("id", 1L)
//        .set("Role", Role.CLUB)
//        .set("deletedAt", null)
//        .sample();
//    User savedUser = userRepository.save(user);
//
//    Club club = fixtureMonkey.giveMeBuilder(Club.class)
//        .set("id", 1L)
//        .set("user", savedUser)
//        .set("score", null)
//        .set("clubMembers", null)
//        .set("deletedAt", null)
//        .sample();
//    clubRepository.save(club);
//
//    List<String> savedSections = new ArrayList<>();
//    savedSections.add("section1");
//    savedSections.add("section2");
//
//    Form form = fixtureMonkey.giveMeBuilder(Form.class)
//        .set("id", 1L)
//        .set("title", "띵동 폼")
//        .set("description", "저희 동아리는 띵동입니다.")
//        .set("hasInterview", false)
//        .set("club", club)
//        .set("sections", savedSections)
//        .sample();
//    Form savedForm = formRepository.save(form);
//
//    FormSectionQuery sectionQuery = facadeUserFormService.getFormSection(savedForm.getId());
//
//    assertThat(sectionQuery.sections()).isEqualTo(savedSections);
//  }
//}
