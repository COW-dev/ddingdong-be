package ddingdong.ddingdongBE.domain.question.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update question set deleted_at = CURRENT_TIMESTAMP where id=?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "question")
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

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

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
