package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import java.util.List;

public class FormFieldFixture {

    public static FormField create(Form form) {
        return FormField.builder()
                .section("섹션")
                .fieldType(FieldType.RADIO)
                .form(form)
                .question("질문")
                .options(List.of("선택지"))
                .required(false)
                .fieldOrder(1)
                .build();
    }

    public static FormField create(Form form, String question) {
        return FormField.builder()
                .section("섹션")
                .fieldType(FieldType.RADIO)
                .form(form)
                .question(question)
                .options(List.of("선택지"))
                .required(false)
                .fieldOrder(1)
                .build();
    }
}
