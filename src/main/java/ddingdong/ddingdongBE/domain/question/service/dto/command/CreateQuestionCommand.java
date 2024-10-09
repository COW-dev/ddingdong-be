package ddingdong.ddingdongBE.domain.question.service.dto.command;

import ddingdong.ddingdongBE.domain.question.entity.Question;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.Builder;

@Builder
public record CreateQuestionCommand(
        User admin,
        String question,
        String reply
) {

    public Question toEntity() {
        return Question.builder()
                .user(this.admin)
                .question(this.question)
                .reply(this.reply).build();
    }

}
