//package ddingdong.ddingdongBE.domain.form.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.navercorp.fixturemonkey.FixtureMonkey;
//import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
//import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
//import ddingdong.ddingdongBE.domain.club.entity.Club;
//import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
//import ddingdong.ddingdongBE.domain.form.entity.Form;
//import ddingdong.ddingdongBE.domain.formapplication.repository.dto.DepartmentInfo;
//import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
//import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
//import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
//import ddingdong.ddingdongBE.domain.formapplication.repository.dto.RecentFormInfo;
//import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
//import ddingdong.ddingdongBE.domain.user.entity.Role;
//import ddingdong.ddingdongBE.domain.user.entity.User;
//import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Slice;
//
//class FormApplicationRepositoryTest extends DataJpaTestSupport {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ClubRepository clubRepository;
//
//    @Autowired
//    private FormRepository formRepository;
//
//    @Autowired
//    private FormApplicationRepository formApplicationRepository;
//
//    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();
//
//    @BeforeEach
//    void setUp() {
//        formApplicationRepository.deleteAllInBatch();
//        clubRepository.deleteAllInBatch();
//        userRepository.deleteAllInBatch();
//        formRepository.deleteAllInBatch();
//    }
//
//    @DisplayName("동아리 폼지의 최신 지원자를 size에 맞춰 반환한다.")
//    @Test
//    void findPageByFormIdOrderById() {
//        // given
//        User user1 = fixture.giveMeBuilder(User.class)
//                .set("id", 1L)
//                .set("role", Role.CLUB)
//                .sample();
//        User user2 = fixture.giveMeBuilder(User.class)
//                .set("id", 2L)
//                .set("role", Role.CLUB)
//                .sample();
//        User savedUser1 = userRepository.save(user1);
//        User savedUser2 = userRepository.save(user2);
//
//        Club club1 = fixture.giveMeBuilder(Club.class)
//                .set("id", 1L)
//                .set("user", savedUser1)
//                .set("score", Score.from(BigDecimal.ZERO))
//                .set("clubMembers", null)
//                .sample();
//        Club club2 = fixture.giveMeBuilder(Club.class)
//                .set("id", 2L)
//                .set("user", savedUser2)
//                .set("score", Score.from(BigDecimal.ZERO))
//                .set("clubMembers", null)
//                .sample();
//        Club savedClub1 = clubRepository.save(club1);
//        Club savedClub2 = clubRepository.save(club2);
//
//        Form form1 = fixture.giveMeBuilder(Form.class)
//                .set("title", "폼지1")
//                .set("description", "저희 동아리는 띵동입니다.")
//                .set("hasInterview", false)
//                .set("club", savedClub1)
//                .set("startDate", LocalDate.now())
//                .set("endDate", LocalDate.now())
//                .sample();
//        Form form2 = fixture.giveMeBuilder(Form.class)
//                .set("title", "폼지2")
//                .set("description", "저희 동아리는 띵동입니다.")
//                .set("hasInterview", false)
//                .set("club", savedClub2)
//                .set("startDate", LocalDate.now())
//                .set("endDate", LocalDate.now())
//                .sample();
//        Form savedForm1 = formRepository.save(form1);
//        Form savedForm2 = formRepository.save(form2);
//
//
//        FormApplication formApplication1 = FormApplication.builder()
//                .name("지원자1")
//                .studentNumber("60201111")
//                .department("융합소프트웨어학부")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm1)
//                .build();
//
//        FormApplication formApplication2 = FormApplication.builder()
//                .name("지원자2")
//                .studentNumber("60201112")
//                .department("융합소프트웨어학부")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm1)
//                .build();
//
//        FormApplication formApplication3 = FormApplication.builder()
//                .name("지원자3")
//                .studentNumber("60201113")
//                .department("디지털콘텐츠디자인학과")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm2)
//                .build();
//
//        FormApplication formApplication4 = FormApplication.builder()
//                .name("지원자4")
//                .studentNumber("60201114")
//                .department("청소년지도학과")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm2)
//                .build();
//
//        FormApplication formApplication5 = FormApplication.builder()
//                .name("지원자5")
//                .studentNumber("60201115")
//                .department("산업공학과")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm1)
//                .build();
//        formApplicationRepository.saveAll(List.of(formApplication1, formApplication2, formApplication3, formApplication4, formApplication5));
//
//        int size = 2;
//        Long currentCursorId = -1L;
//        // when
//        Slice<FormApplication> newestFormApplications = formApplicationRepository.findPageByFormIdOrderById(savedForm1.getId(), size, currentCursorId);
//        // then
//        List<FormApplication> retrievedApplications = newestFormApplications.getContent();
//        assertThat(retrievedApplications.size()).isEqualTo(2);
//        assertThat(retrievedApplications.get(0).getId()).isGreaterThan(retrievedApplications.get(1).getId());
//    }
//
//
//    @DisplayName("지원자 수 상위 5개 학과와 그 개수를 반환한다.")
//    @Test
//    void findTopFiveDepartmentsByForm_ShouldReturnTopFiveDepartments() throws InterruptedException {
//        // given
//        Form form = fixture.giveMeBuilder(Form.class)
//                .set("club", null)
//                .set("sections", List.of("공통"))
//                .sample();
//        Form savedForm = formRepository.save(form);
//        FormApplication formApplication = FormApplication.builder()
//                .name("이름1")
//                .studentNumber("학번1")
//                .department("학과1")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm)
//                .build();
//        FormApplication formApplication2 = FormApplication.builder()
//                .name("이름1")
//                .studentNumber("학번1")
//                .department("학과2")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm)
//                .build();
//        FormApplication formApplication3 = FormApplication.builder()
//                .name("이름1")
//                .studentNumber("학번1")
//                .department("학과2")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm)
//                .build();
//        FormApplication formApplication4 = FormApplication.builder()
//                .name("이름1")
//                .studentNumber("학번1")
//                .department("학과3")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm)
//                .build();
//        FormApplication formApplication5 = FormApplication.builder()
//                .name("이름1")
//                .studentNumber("학번1")
//                .department("학과3")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm)
//                .build();
//        FormApplication formApplication6 = FormApplication.builder()
//                .name("이름1")
//                .studentNumber("학번1")
//                .department("학과3")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm)
//                .build();
//
//        formApplicationRepository.saveAll(
//                List.of(formApplication, formApplication2, formApplication3, formApplication4, formApplication5,
//                        formApplication6)
//        );
//        // when
//        List<DepartmentInfo> topFive = formApplicationRepository.findTopDepartmentsByFormId(savedForm.getId(),5);
//        // then
//
//        assertThat(topFive.size()).isEqualTo(3);
//        assertThat(topFive.get(0).getCount()).isEqualTo(3);
//        assertThat(topFive.get(0).getDepartment()).isEqualTo("학과3");
//        assertThat(topFive.get(1).getCount()).isEqualTo(2);
//        assertThat(topFive.get(1).getDepartment()).isEqualTo("학과2");
//        assertThat(topFive.get(2).getCount()).isEqualTo(1);
//        assertThat(topFive.get(2).getDepartment()).isEqualTo("학과1");
//    }
//
//    @DisplayName("주어진 날짜를 기준으로 주어진 개수만큼 최신 폼지의 시작일과 지원 수를 반환한다.")
//    @Test
//    void findRecentFormByDateWithApplicationCount() {
//        // given
//        Club club1 = fixture.giveMeBuilder(Club.class)
//                .set("id", 1L)
//                .set("user", null)
//                .set("score", Score.from(BigDecimal.ZERO))
//                .set("clubMembers", null)
//                .sample();
//        Club savedClub = clubRepository.save(club1);
//        Form form = fixture.giveMeBuilder(Form.class)
//                .set("id", 1L)
//                .set("club", savedClub)
//                .set("startDate", LocalDate.of(2020, 3, 1))
//                .set("endDate", LocalDate.of(2020, 4, 1))
//                .set("sections", List.of("공통"))
//                .sample();
//        Form savedForm = formRepository.save(form);
//        FormApplication formApplication = FormApplication.builder()
//                .name("이름1")
//                .studentNumber("학번1")
//                .department("학과1")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm)
//                .build();
//        FormApplication formApplication2 = FormApplication.builder()
//                .name("이름1")
//                .studentNumber("학번1")
//                .department("학과1")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm)
//                .build();
//
//        formApplicationRepository.saveAll(List.of(formApplication, formApplication2));
//
//        Form form2 = fixture.giveMeBuilder(Form.class)
//                .set("id", 2L)
//                .set("club", savedClub)
//                .set("startDate", LocalDate.of(2020, 1, 1))
//                .set("endDate", LocalDate.of(2020, 2, 1))
//                .set("sections", List.of("공통"))
//                .sample();
//        Form savedForm2 = formRepository.save(form2);
//        FormApplication formApplication3 = FormApplication.builder()
//                .name("이름1")
//                .studentNumber("학번1")
//                .department("학과1")
//                .status(FormApplicationStatus.SUBMITTED)
//                .form(savedForm2)
//                .build();
//        formApplicationRepository.save(formApplication3);
//
//        Form form3 = fixture.giveMeBuilder(Form.class)
//                .set("id", 1L)
//                .set("club", savedClub)
//                .set("startDate", LocalDate.of(2020, 5, 1))
//                .set("endDate", LocalDate.of(2020, 6, 1))
//                .set("sections", List.of("공통"))
//                .sample();
//        formRepository.save(form3);
//        // when
//        List<RecentFormInfo> recentFormInfos = formApplicationRepository.findRecentFormByDateWithApplicationCount(
//                savedClub.getId(),
//                savedForm.getEndDate(),
//                3
//        );
//        // then
//        assertThat(recentFormInfos.size()).isEqualTo(2);
//        assertThat(recentFormInfos.get(0).getCount()).isEqualTo(1);
//        assertThat(recentFormInfos.get(0).getDate()).isEqualTo(LocalDate.of(2020, 1, 1));
//        assertThat(recentFormInfos.get(1).getCount()).isEqualTo(2);
//        assertThat(recentFormInfos.get(1).getDate()).isEqualTo(LocalDate.of(2020, 3, 1));
//
//
//    }
//}
