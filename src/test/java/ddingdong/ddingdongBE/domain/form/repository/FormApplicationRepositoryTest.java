package ddingdong.ddingdongBE.domain.form.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.DepartmentInfo;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.RecentFormInfo;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.Role;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FormApplicationRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private FormApplicationRepository formApplicationRepository;

    @BeforeEach
    void setUp() {
        formApplicationRepository.deleteAllInBatch();
        clubRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        formRepository.deleteAllInBatch();
    }

    @DisplayName("특정 동아리 폼지 지원자의 학과 중 많은 학과 상위 n개를 조회한다.")
    @Test
    void findTopDepartmentsByFormId() {
        // given
        User user = User.builder()
                .name("유저")
                .role(Role.CLUB)
                .build();
        User savedUser = userRepository.save(user);

        Club club = Club.builder()
                .name("동아리")
                .user(savedUser)
                .score(Score.from(BigDecimal.ZERO))
                .build();
        Club savedClub = clubRepository.save(club);

        Form form = Form.builder()
                .title("폼지")
                .description("설명")
                .hasInterview(false)
                .club(savedClub)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .sections(List.of("섹션1"))
                .build();
        Form savedForm = formRepository.save(form);

        List<FormApplication> applications = List.of(
                createApp(savedForm, "융합소프트웨어학부"),
                createApp(savedForm, "융합소프트웨어학부"),
                createApp(savedForm, "디지털콘텐츠디자인학과"),
                createApp(savedForm, "청소년지도학과"),
                createApp(savedForm, "산업공학과"),
                createApp(savedForm, "디지털콘텐츠디자인학과"),
                createApp(savedForm, "융합소프트웨어학부")
        );
        formApplicationRepository.saveAll(applications);

        // when
        List<DepartmentInfo> result = formApplicationRepository.findTopDepartmentsByFormId(
                savedForm.getId(), 2);

        // then
        assertThat(result).hasSize(2);

        // 1위 검증
        assertThat(result.get(0).getDepartment()).isEqualTo("융합소프트웨어학부");
        assertThat(result.get(0).getCount()).isEqualTo(3L);

        // 2위 검증
        assertThat(result.get(1).getDepartment()).isEqualTo("디지털콘텐츠디자인학과");
        assertThat(result.get(1).getCount()).isEqualTo(2L);
    }

    @DisplayName("주어진 날짜를 기준으로 주어진 개수만큼 최신 폼지의 시작일과 지원 수를 반환한다.")
    @Test
    void findRecentFormByDateWithApplicationCount() {
        // given
        User user = User.builder()
                .name("유저")
                .role(Role.CLUB)
                .build();
        User savedUser = userRepository.save(user);

        Club club = Club.builder()
                .name("동아리")
                .user(savedUser)
                .score(Score.from(BigDecimal.ZERO))
                .build();
        Club savedClub = clubRepository.save(club);

        Form form1 = Form.builder()
                .title("폼지")
                .description("설명")
                .hasInterview(false)
                .club(savedClub)
                .startDate(LocalDate.of(2020, 3, 1))
                .endDate(LocalDate.of(2020, 4, 1))
                .sections(List.of("섹션1"))
                .build();
        Form savedForm1 = formRepository.save(form1);

        FormApplication formApplication = FormApplication.builder()
                .name("이름1")
                .studentNumber("6000000")
                .email("email@email.com")
                .phoneNumber("010-0000-000")
                .department("학과1")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm1)
                .build();
        FormApplication formApplication2 = FormApplication.builder()
                .name("이름1")
                .studentNumber("6000000")
                .email("email@email.com")
                .phoneNumber("010-0000-000")
                .department("학과1")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm1)
                .build();

        formApplicationRepository.saveAll(List.of(formApplication, formApplication2));

        Form form2 = Form.builder()
                .title("폼지")
                .description("설명")
                .hasInterview(false)
                .club(savedClub)
                .startDate(LocalDate.of(2020, 1, 1))
                .endDate(LocalDate.of(2020, 2, 1))
                .sections(List.of("섹션1"))
                .build();
        Form savedForm2 = formRepository.save(form2);

        FormApplication formApplication3 = FormApplication.builder()
                .name("이름1")
                .studentNumber("6000000")
                .email("email@email.com")
                .phoneNumber("010-0000-000")
                .department("학과1")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm2)
                .build();
        formApplicationRepository.save(formApplication3);

        Form form3 = Form.builder()
                .title("폼지")
                .description("설명")
                .hasInterview(false)
                .club(savedClub)
                .startDate(LocalDate.of(2020, 5, 1))
                .endDate(LocalDate.of(2020, 6, 1))
                .sections(List.of("섹션1"))
                .build();
        Form savedForm3 = formRepository.save(form3);

        // when
        List<RecentFormInfo> recentFormInfos = formApplicationRepository.findRecentFormByDateWithApplicationCount(
                savedClub.getId(),
                savedForm1.getEndDate(),
                3
        );
        // then
        assertThat(recentFormInfos.size()).isEqualTo(2);
        assertThat(recentFormInfos.get(0).getCount()).isEqualTo(1);
        assertThat(recentFormInfos.get(0).getDate()).isEqualTo(LocalDate.of(2020, 1, 1));
        assertThat(recentFormInfos.get(1).getCount()).isEqualTo(2);
        assertThat(recentFormInfos.get(1).getDate()).isEqualTo(LocalDate.of(2020, 3, 1));
    }

    @DisplayName("특정 동아리 폼지의 모든 최종 합격자 리스트를 반환한다.")
    @Test
    void findAllFinalPassedByFormId() {
        // given
        User user = User.builder()
                .name("유저")
                .role(Role.CLUB)
                .build();
        User savedUser = userRepository.save(user);

        Club club = Club.builder()
                .name("동아리")
                .user(savedUser)
                .score(Score.from(BigDecimal.ZERO))
                .build();
        Club savedClub = clubRepository.save(club);

        Form form = Form.builder()
                .title("폼지")
                .description("설명")
                .hasInterview(false)
                .club(savedClub)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .sections(List.of("섹션1"))
                .build();
        Form savedForm = formRepository.save(form);

        List<FormApplication> applications = List.of(
                createApp(savedForm, FormApplicationStatus.FINAL_PASS),
                createApp(savedForm, FormApplicationStatus.FINAL_PASS),
                createApp(savedForm, FormApplicationStatus.FINAL_PASS),
                createApp(savedForm, FormApplicationStatus.FIRST_PASS),
                createApp(savedForm, FormApplicationStatus.FINAL_FAIL),
                createApp(savedForm, FormApplicationStatus.FINAL_FAIL),
                createApp(savedForm, FormApplicationStatus.SUBMITTED)
        );
        formApplicationRepository.saveAll(applications);

        // when
        formApplicationRepository.findAllFinalPassedByFormId(savedForm.getId());

        // then
        assertThat(formApplicationRepository.findAllFinalPassedByFormId(savedForm.getId())
                .size()).isEqualTo(4);
    }

    private FormApplication createApp(Form form, String department) {
        return FormApplication.builder()
                .name("지원자")
                .studentNumber("60000000")
                .phoneNumber("010-0000-000")
                .email("email@email.com")
                .department(department)
                .status(FormApplicationStatus.SUBMITTED)
                .form(form)
                .build();
    }

    private FormApplication createApp(Form form, FormApplicationStatus status) {
        return FormApplication.builder()
                .name("지원자")
                .studentNumber("60000000")
                .phoneNumber("010-0000-000")
                .email("email@email.com")
                .department("학과1")
                .status(status)
                .form(form)
                .build();
    }
}