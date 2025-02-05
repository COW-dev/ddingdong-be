package ddingdong.ddingdongBE.domain.form.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.repository.FormFieldRepository;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.ApplicantRateQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.DepartmentRankQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.FieldStatisticsListQuery;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormAnswerRepository;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FormStatisticServiceImplTest extends TestContainerSupport {

    @Autowired
    private FormApplicationRepository formApplicationRepository;

    @Autowired
    private FormStatisticService formStatisticService;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private FormFieldRepository formFieldRepository;

    @Autowired
    private FormAnswerRepository formAnswerRepository;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("폼지에 지원한 총 지원자 수를 반환한다.")
    @Test
    void getTotalApplicationCountByForm() {
        // given
        Form form = fixture.giveMeBuilder(Form.class)
                .set("club", null)
                .set("sections", List.of("공통"))
                .sample();
        Form savedForm = formRepository.save(form);
        FormApplication formApplication = FormApplication.builder()
                .name("이름1")
                .studentNumber("학번1")
                .department("학과1")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm)
                .build();
        FormApplication formApplication2 = FormApplication.builder()
                .name("이름1")
                .studentNumber("학번1")
                .department("학과2")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm)
                .build();
        FormApplication formApplication3 = FormApplication.builder()
                .name("이름1")
                .studentNumber("학번1")
                .department("학과2")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm)
                .build();
        formApplicationRepository.saveAll(List.of(formApplication, formApplication2, formApplication3));
        // when
        int count = formStatisticService.getTotalApplicationCountByForm(savedForm);
        // then
        assertThat(count).isEqualTo(3);
    }

    @DisplayName("폼지 내 지원자 수가 높은 학과 순으로 관련 정보를 정해진 수만큼 반환한다.")
    @Test
    void createDepartmentRankByForm() {
        // given
        Form form = fixture.giveMeBuilder(Form.class)
                .set("club", null)
                .set("sections", List.of("공통"))
                .sample();
        Form savedForm = formRepository.save(form);
        FormApplication formApplication = FormApplication.builder()
                .name("이름1")
                .studentNumber("학번1")
                .department("학과1")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm)
                .build();
        FormApplication formApplication2 = FormApplication.builder()
                .name("이름1")
                .studentNumber("학번1")
                .department("학과2")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm)
                .build();
        FormApplication formApplication3 = FormApplication.builder()
                .name("이름1")
                .studentNumber("학번1")
                .department("학과2")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm)
                .build();
        FormApplication formApplication4 = FormApplication.builder()
                .name("이름1")
                .studentNumber("학번1")
                .department("학과3")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm)
                .build();
        FormApplication formApplication5 = FormApplication.builder()
                .name("이름1")
                .studentNumber("학번1")
                .department("학과3")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm)
                .build();
        FormApplication formApplication6 = FormApplication.builder()
                .name("이름1")
                .studentNumber("학번1")
                .department("학과3")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm)
                .build();

        formApplicationRepository.saveAll(
                List.of(formApplication, formApplication2, formApplication3, formApplication4, formApplication5,
                        formApplication6)
        );
        // when
        List<DepartmentRankQuery> departmentRanks = formStatisticService.createDepartmentRankByForm(savedForm);
        // then
        assertThat(departmentRanks.size()).isEqualTo(3);
        assertThat(departmentRanks.get(0).rank()).isEqualTo(1);
        assertThat(departmentRanks.get(0).label()).isEqualTo("학과3");
        assertThat(departmentRanks.get(0).count()).isEqualTo(3);
        assertThat(departmentRanks.get(1).rank()).isEqualTo(2);
        assertThat(departmentRanks.get(1).label()).isEqualTo("학과2");
        assertThat(departmentRanks.get(1).count()).isEqualTo(2);
        assertThat(departmentRanks.get(2).rank()).isEqualTo(3);
        assertThat(departmentRanks.get(2).label()).isEqualTo("학과1");
        assertThat(departmentRanks.get(2).count()).isEqualTo(1);

    }

    @DisplayName("전 반기에 대한 지원 비율 정보를 정해진 개수만큼 반환한다")
    @Test
    void createApplicationRateByForm() {
        // given
        Form formFirst = fixture.giveMeBuilder(Form.class)
                .set("id", 1L)
                .set("club", null)
                .set("startDate", LocalDate.of(2020, 2, 1))
                .set("endDate", LocalDate.of(2020, 3, 1))
                .set("sections", List.of("공통"))
                .sample();
        Form savedForm = formRepository.save(formFirst);
        FormApplication formApplication = FormApplication.builder()
                .name("이름1")
                .studentNumber("학번1")
                .department("학과1")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm)
                .build();
        FormApplication formApplication2 = FormApplication.builder()
                .name("이름1")
                .studentNumber("학번1")
                .department("학과1")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm)
                .build();

        formApplicationRepository.saveAll(List.of(formApplication, formApplication2));

        Form form2 = fixture.giveMeBuilder(Form.class)
                .set("id", 2L)
                .set("club", null)
                .set("startDate", LocalDate.of(2020, 7, 3))
                .set("endDate", LocalDate.of(2020, 8, 1))
                .set("sections", List.of("공통"))
                .sample();
        Form savedForm2 = formRepository.save(form2);
        FormApplication formApplication3 = FormApplication.builder()
                .name("이름1")
                .studentNumber("학번1")
                .department("학과1")
                .status(FormApplicationStatus.SUBMITTED)
                .form(savedForm2)
                .build();
        formApplicationRepository.save(formApplication3);
        // when
        List<ApplicantRateQuery> applicationRateQueries = formStatisticService.createApplicationRateByForm(savedForm2);
        // then

        assertThat(applicationRateQueries.size()).isEqualTo(2);
        assertThat(applicationRateQueries.get(0).label()).isEqualTo("2020-1");
        assertThat(applicationRateQueries.get(0).count()).isEqualTo(2);
        assertThat(applicationRateQueries.get(0).comparedToLastSemester()).isEqualTo(100);
        assertThat(applicationRateQueries.get(1).label()).isEqualTo("2020-2");
        assertThat(applicationRateQueries.get(1).count()).isEqualTo(1);
        assertThat(applicationRateQueries.get(1).comparedToLastSemester()).isEqualTo(50);


    }

    @DisplayName("폼지의 질문 정보")
    @Test
    void createFieldStatisticsListByForm() {
        // given
        Form formFirst = fixture.giveMeBuilder(Form.class)
                .set("id", 1L)
                .set("club", null)
                .set("startDate", LocalDate.of(2020, 2, 1))
                .set("endDate", LocalDate.of(2020, 3, 1))
                .set("sections", List.of("공통"))
                .sample();
        Form savedForm = formRepository.save(formFirst);
        FormField formField = FormField.builder()
                .question("설문 질문")
                .required(true)
                .fieldOrder(1)
                .section("기본 정보")
                .options(List.of("옵션1", "옵션2", "옵션3"))
                .fieldType(FieldType.TEXT)
                .form(savedForm)
                .build();
        FormField savedField = formFieldRepository.save(formField);

        FormAnswer answer = FormAnswer.builder()
                .formApplication(null)
                .value(null)
                .formField(savedField)
                .build();
        FormAnswer answer2 = FormAnswer.builder()
                .formApplication(null)
                .value(null)
                .formField(savedField)
                .build();
        FormAnswer answer3 = FormAnswer.builder()
                .formApplication(null)
                .value(null)
                .formField(savedField)
                .build();
        formAnswerRepository.saveAll(List.of(answer, answer2, answer3));
        // when
        List<FieldStatisticsListQuery> fieldStatisticsList = formStatisticService.createFieldStatisticsListByForm(
                savedForm);
        // then
        assertThat(fieldStatisticsList.size()).isEqualTo(1);
        assertThat(fieldStatisticsList.get(0).count()).isEqualTo(3);
        assertThat(fieldStatisticsList.get(0).question()).isEqualTo("설문 질문");
    }
}
