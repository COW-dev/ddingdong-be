package ddingdong.ddingdongBE.email.entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        return (int) emailSendHistories.stream()
                .filter(EmailSendHistory::isFail)
                .count();
    }

    public EmailSendHistories getLatestByFormApplication() {
        Map<Long, EmailSendHistory> latestByApplicationId = emailSendHistories.stream()
                .collect(Collectors.toMap(
                        history -> history.getFormApplication().getId(),
                        history -> history,
                        EmailSendHistories::pickLatest
                ));

        return new EmailSendHistories(List.copyOf(latestByApplicationId.values()));
    }

    private static EmailSendHistory pickLatest(EmailSendHistory a, EmailSendHistory b) {
        Comparator<EmailSendHistory> comparator = Comparator
                .comparing(EmailSendHistory::getSentAt,
                        Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(EmailSendHistory::getId,
                        Comparator.nullsFirst(Comparator.naturalOrder()));

        return comparator.compare(a, b) >= 0 ? a : b;
    }

    public List<EmailSendHistory> getAll() {
        return Collections.unmodifiableList(emailSendHistories);
    }
}
