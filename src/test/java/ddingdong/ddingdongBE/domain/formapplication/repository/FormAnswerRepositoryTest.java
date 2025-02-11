package ddingdong.ddingdongBE.domain.formapplication.repository;

import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.repository.FormFieldRepository;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FormAnswerRepositoryTest extends DataJpaTestSupport {

    @Autowired
    FormAnswerRepository formAnswerRepository;
    @Autowired
    FormFieldRepository formFieldRepository;

    @DisplayName("주어진 FormField와 연관된 FormAnswer의 value를 모두 반환한다.")
    @Test
    void findAllValueByFormFieldId() {
        // given
        FormField formField = FormField.builder()
                .question("질문입니다")
                .required(true)
                .fieldOrder(1)
                .section("서버")
                .options(List.of("지문1", "지문2"))
                .fieldType(FieldType.CHECK_BOX)
                .form(null)
                .build();
        FormField savedField = formFieldRepository.save(formField);
        FormAnswer formAnswer = FormAnswer.builder()
                .value(List.of("서버", "웹"))
                .formField(savedField)
                .build();
        FormAnswer formAnswer2 = FormAnswer.builder()
                .value(List.of("서버입니다", "웹입니다."))
                .formField(savedField)
                .build();
        formAnswerRepository.saveAll(List.of(formAnswer, formAnswer2));
        // when
        List<String> allValueByFormField = formAnswerRepository.findAllValueByFormFieldId(savedField.getId());
        // then
        Assertions.assertThat(allValueByFormField).hasSize(2);
        Assertions.assertThat(allValueByFormField.get(0)).isEqualTo("[\"서버\",\"웹\"]");
    }
}
