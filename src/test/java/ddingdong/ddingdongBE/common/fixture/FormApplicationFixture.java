package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;

public class FormApplicationFixture {

    public static FormApplication create(String name, Form form) {
        return FormApplication.builder()
                .name(name)
                .studentNumber("20231234")
                .department("컴퓨터공학과")
                .phoneNumber("010-1234-5678")
                .email("example@email.com")
                .status(FormApplicationStatus.SUBMITTED) // 예시 상태, 필요에 따라 바꿔도 됨
                .form(form)
                .deletedAt(null)
                .build();
    }
}
