package ddingdong.ddingdongBE.email.entity;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailSendHistories {

    private final List<EmailSendHistory> emailSendHistories;

    public int getTotalCount() {
        return emailSendHistories.size();
    }

    public int getSuccessCount() {
        return (int) emailSendHistories.stream()
                .filter(EmailSendHistory::isSuccess)
                .count();
    }

    public int getFailCount() {
        return  (int) emailSendHistories.stream()
                .filter(EmailSendHistory::isFail)
                .count();
    }
}
