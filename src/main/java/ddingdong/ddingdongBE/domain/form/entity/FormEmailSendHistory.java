package ddingdong.ddingdongBE.domain.form.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FormEmailSendHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Enumerated(EnumType.STRING)
    private FormApplicationStatus formApplicationStatus;

    @Column(columnDefinition = "TEXT")
    private String emailContent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Form form;

    @Builder
    public FormEmailSendHistory(String title, FormApplicationStatus formApplicationStatus, String emailContent, Form form) {
        this.title = title;
        this.formApplicationStatus = formApplicationStatus;
        this.emailContent = emailContent;
        this.form = form;
    }
}
