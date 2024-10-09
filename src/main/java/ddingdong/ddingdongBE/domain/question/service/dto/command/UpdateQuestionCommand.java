package ddingdong.ddingdongBE.domain.question.service.dto.command;

import ddingdong.ddingdongBE.domain.question.entity.Question;
import lombok.Builder;

@Builder
public record UpdateQuestionCommand(
        Long questionId,
        String question,
        String reply
) {

    public Question toEntity() {
        return Question.builder()
                .question(this.question)
                .reply(this.reply)
                .build();
    }
}
