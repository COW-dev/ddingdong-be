package ddingdong.ddingdongBE.domain.form.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.repository.dto.FieldListInfo;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormAnswerRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FormFieldRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private FormFieldRepository formFieldRepository;

    @Autowired
    private FormAnswerRepository formAnswerRepository;

    private static final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("원하는 필드 정보와 해당 필드의 답변 개수를 반환한다.")
    @Test
    void findFieldWithAnswerCountByFormId() {
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
        List<FieldListInfo> fieldListInfos = formFieldRepository.findFieldWithAnswerCountByFormId(
                savedForm.getId());
        // then
        assertThat(fieldListInfos.size()).isEqualTo(1);
        assertThat(fieldListInfos.get(0).getQuestion()).isEqualTo(formField.getQuestion());
        assertThat(fieldListInfos.get(0).getCount()).isEqualTo(3);
    }
}
