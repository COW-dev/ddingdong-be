package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import java.util.List;

public class FormAnswerFixture {

    public static FormAnswer create(FormApplication formApplication, FormField formField) {
        return FormAnswer.builder()
                .formApplication(formApplication)
                .formField(formField)
                .value(List.of("답변"))
                .build();
    }

    public static FormAnswer create(FormApplication formApplication, FormField formField, List<String> value) {
        return FormAnswer.builder()
                .formApplication(formApplication)
                .formField(formField)
                .value(value)
                .build();
    }
}
