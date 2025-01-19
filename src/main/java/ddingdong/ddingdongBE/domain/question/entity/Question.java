package ddingdong.ddingdongBE.domain.question.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update question set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
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

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    private Question(Long id, User user, String question, String reply, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.question = question;
        this.reply = reply;
        super.setCreatedAt(createdAt);
    }

    public void updateQuestion(Question updatedQuestion) {
        this.question = updatedQuestion.getQuestion();
        this.reply = updatedQuestion.getReply();
    }
}
