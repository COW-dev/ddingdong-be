package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormEmailSendHistory;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;

public class FormEmailSendHistoryFixture {

    public static FormEmailSendHistory createFormEmailSendHistory(Form form, FormApplicationStatus status) {
        return FormEmailSendHistory.builder()
                .form(form)
                .formApplicationStatus(status)
                .title("테스트 이메일 제목입니다.")
                .emailContent("테스트 이메일 내용입니다.")
                .build();
    }

    public static FormEmailSendHistory createFormEmailSendHistoryForFirstPass(Form form) {
        return createFormEmailSendHistory(form, FormApplicationStatus.FIRST_PASS);
    }

    public static FormEmailSendHistory createFormEmailSendHistoryForFinalPass(Form form) {
        return createFormEmailSendHistory(form, FormApplicationStatus.FINAL_PASS);
    }

    public static FormEmailSendHistory createFormEmailSendHistoryForFirstFail(Form form) {
        return createFormEmailSendHistory(form, FormApplicationStatus.FIRST_FAIL);
    }

    public static FormEmailSendHistory createFormEmailSendHistoryForFinalFail(Form form) {
        return createFormEmailSendHistory(form, FormApplicationStatus.FINAL_FAIL);
    }
}