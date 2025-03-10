package ddingdong.ddingdongBE.domain.formapplication.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "update form_application set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class FormApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String studentNumber;

    @Column(nullable = false, length = 50)
    private String department;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private FormApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Form form;

    @Column(length = 500)
    private String note;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    private FormApplication(
            String name,
            String studentNumber,
            String department,
            String phoneNumber,
            String email,
            FormApplicationStatus status,
            Form form,
            LocalDateTime deletedAt
    ) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.department = department;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.status = status;
        this.form = form;
        this.deletedAt = deletedAt;
    }

    public void updateStatus(FormApplicationStatus status) {
        this.status = status;
    }

    public void updateNote(String note) {
        this.note = note;
    }
}
