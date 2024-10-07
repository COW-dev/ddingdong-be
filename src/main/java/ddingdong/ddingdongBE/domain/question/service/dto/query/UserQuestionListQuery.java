package ddingdong.ddingdongBE.domain.question.service.dto.query;

import ddingdong.ddingdongBE.domain.question.entity.Question;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UserQuestionListQuery(
        Long id,
        String question,
        String reply,
        LocalDate createdAt
) {

    public static UserQuestionListQuery from(Question question) {
        return UserQuestionListQuery.builder()
                .id(question.getId())
                .question(question.getQuestion())
                .reply(question.getReply())
                .createdAt(question.getCreatedAt().toLocalDate())
                .build();
    }
}
