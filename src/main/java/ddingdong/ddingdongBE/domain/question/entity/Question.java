package ddingdong.ddingdongBE.domain.question.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String reply;

    @Builder
    private Question(Long id, User user, String question, String reply, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.question = question;
        this.reply = reply;
        super.setCreatedAt(createdAt);
    }

    public void updateQuestion(Question updatedDocument) {
        this.question = updatedDocument.getQuestion();
        this.reply = updatedDocument.getReply();
    }
}
