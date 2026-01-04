package ddingdong.ddingdongBE.domain.formapplication.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FormApplicationFixture;
import ddingdong.ddingdongBE.common.fixture.FormFixture;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.DepartmentInfo;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.RecentFormInfo;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
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

    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    @Autowired
    private FormAnswerRepository formAnswerRepository;

    @BeforeEach
    void setUp() {
        formApplicationRepository.deleteAllInBatch();
        clubRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        formRepository.deleteAllInBatch();
        fileMetaDataRepository.deleteAllInBatch();
        formAnswerRepository.deleteAllInBatch();
    }

    @DisplayName("특정 동아리 폼지 지원자의 학과 중 많은 학과 상위 n개를 조회한다.")
    @Test
    void findTopDepartmentsByFormId() {
        // given
        Club savedClub = clubRepository.save(ClubFixture.createClub());
        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));

        List<FormApplication> applications = List.of(
                FormApplicationFixture.createWithDepartment(savedForm, "융합소프트웨어학부"),
                FormApplicationFixture.createWithDepartment(savedForm, "융합소프트웨어학부"),
                FormApplicationFixture.createWithDepartment(savedForm, "디지털콘텐츠디자인학과"),
                FormApplicationFixture.createWithDepartment(savedForm, "청소년지도학과"),
                FormApplicationFixture.createWithDepartment(savedForm, "산업공학과"),
                FormApplicationFixture.createWithDepartment(savedForm, "디지털콘텐츠디자인학과"),
                FormApplicationFixture.createWithDepartment(savedForm, "융합소프트웨어학부")
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
        Club savedClub = clubRepository.save(ClubFixture.createClub());

        Form savedForm1 = formRepository.save(FormFixture.createForm(savedClub, List.of(2020, 3, 1), List.of(2020, 4, 1)));
        FormApplication formApplication1 = FormApplicationFixture.create(savedForm1);
        FormApplication formApplication2 = FormApplicationFixture.create(savedForm1);
        formApplicationRepository.saveAll(List.of(formApplication1, formApplication2));

        Form savedForm2 = formRepository.save(FormFixture.createForm(savedClub, List.of(2020, 1, 1), List.of(2020, 2, 1)));
        FormApplication formApplication3 = FormApplicationFixture.create(savedForm2);
        formApplicationRepository.save(formApplication3);

        Form savedForm3 = formRepository.save(FormFixture.createForm(savedClub, List.of(2020, 5, 1), List.of(2020, 6, 1)));

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
        Club savedClub = clubRepository.save(ClubFixture.createClub());
        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));

        List<FormApplication> applications = List.of(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FINAL_PASS),
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FINAL_PASS),
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FINAL_PASS),
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FIRST_PASS),
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FINAL_FAIL),
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FINAL_FAIL),
                FormApplicationFixture.create(savedForm, FormApplicationStatus.SUBMITTED)
        );
        formApplicationRepository.saveAll(applications);

        // when
        List<FormApplication> finalPassedApplications =
                formApplicationRepository.findAllFinalPassedByFormId(savedForm.getId());

        // then
        assertThat(finalPassedApplications).hasSize(3);
    }
}
