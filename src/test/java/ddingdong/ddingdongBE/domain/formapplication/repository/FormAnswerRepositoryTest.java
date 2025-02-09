package ddingdong.ddingdongBE.domain.formapplication.repository;

import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FormAnswerRepositoryTest extends DataJpaTestSupport {

    @Autowired
    FormAnswerRepository formAnswerRepository;

    @DisplayName("해당 option을 선택한 answer의 개수를 반환한다.")
    @Test
    void countAnswerByOption() {
        // given
        FormAnswer formAnswer = FormAnswer.builder()
                .value(List.of("서버", "웹입니다."))
                .build();
        FormAnswer formAnswer2 = FormAnswer.builder()
                .value(List.of("서버", "웹입니다."))
                .build();
        FormAnswer formAnswer3 = FormAnswer.builder()
                .value(List.of("서버", "웹입니다."))
                .build();
        FormAnswer formAnswer4 = FormAnswer.builder()
                .value(List.of("서버입니다.", "웹입니다."))
                .build();
        FormAnswer formAnswer5 = FormAnswer.builder()
                .value(List.of("서버입니다.", "웹입니다."))
                .build();
        formAnswerRepository.saveAll(List.of(formAnswer, formAnswer2, formAnswer3, formAnswer4, formAnswer5));
        // when
        Integer count = formAnswerRepository.countAnswerByOption("서버");
        // then
        Assertions.assertThat(count).isEqualTo(3);
    }
}
