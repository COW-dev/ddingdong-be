package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import java.time.LocalDate;
import java.util.List;

public class FormFixture {

    public static Form formWithClubNull() {
        return Form.builder()
                .title("모집 지원서")
                .description("동아리 모집을 위한 지원서입니다.")
                .startDate(LocalDate.of(2025, 4, 1))
                .endDate(LocalDate.of(2025, 4, 15))
                .hasInterview(true)
                .sections(List.of("자기소개", "지원 동기", "경력 및 경험"))
                .build();
    }

    public static FormField formFieldWithFormNull() {
        return FormField.builder()
                .question("자기소개를 해주세요.")
                .required(true)
                .fieldOrder(1)
                .section("기본 정보")
                .options(List.of("없음"))
                .fieldType(FieldType.TEXT)
                .form(null)
                .build();
    }

    public static FormAnswer formAnswerByFormField(FormField savedFormField) {
        return FormAnswer.builder()
                .value(List.of("답변"))
                .formField(savedFormField)
                .build();
    }
}
