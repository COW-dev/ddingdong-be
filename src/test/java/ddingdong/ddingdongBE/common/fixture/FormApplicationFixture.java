package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import org.springframework.test.util.ReflectionTestUtils;

public class FormApplicationFixture {

    public static FormApplication create(Form form) {
        return FormApplication.builder()
                .name("이름")
                .studentNumber("20231234")
                .department("학과")
                .phoneNumber("010-1234-5678")
                .email("example@email.com")
                .status(FormApplicationStatus.SUBMITTED)
                .form(form)
                .deletedAt(null)
                .build();
    }

    public static FormApplication create(Form form, String name, String department) {
        return FormApplication.builder()
                .name(name)
                .studentNumber("20231234")
                .department(department)
                .phoneNumber("010-1234-5678")
                .email("example@email.com")
                .status(FormApplicationStatus.SUBMITTED) // 예시 상태, 필요에 따라 바꿔도 됨
                .form(form)
                .deletedAt(null)
                .build();
    }

    public static FormApplication create(Form form, FormApplicationStatus status) {
        return FormApplication.builder()
                .name("이름")
                .studentNumber("20231234")
                .department("학과")
                .phoneNumber("010-1234-5678")
                .email("example@email.com")
                .status(status)
                .form(form)
                .deletedAt(null)
                .build();
    }

    public static FormApplication pendingFormApplication() {
        return FormApplication.builder()
                .name("이름")
                .studentNumber("20231234")
                .department("컴퓨터공학과")
                .phoneNumber("010-1234-5678")
                .email("example@email.com")
                .status(FormApplicationStatus.SUBMITTED) // 예시 상태, 필요에 따라 바꿔도 됨
                .deletedAt(null)
                .build();
    }

    public static FormApplication firstPassFormApplication() {
        return FormApplication.builder()
                .name("이름")
                .studentNumber("20231234")
                .department("컴퓨터공학과")
                .phoneNumber("010-1234-5678")
                .email("example@email.com")
                .status(FormApplicationStatus.FIRST_PASS) // 예시 상태, 필요에 따라 바꿔도 됨
                .deletedAt(null)
                .build();
    }

    public static FormApplication createWithId(Long id) {
        FormApplication formApplication = FormApplication.builder()
                .name("테스트")
                .studentNumber("20240001")
                .department("테스트학과")
                .phoneNumber("010-1234-5678")
                .email("test@test.com")
                .status(FormApplicationStatus.SUBMITTED)
                .build();
        ReflectionTestUtils.setField(formApplication, "id", id);
        return formApplication;
    }
}
