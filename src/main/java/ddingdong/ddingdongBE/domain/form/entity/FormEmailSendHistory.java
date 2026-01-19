package ddingdong.ddingdongBE.domain.form.entity;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
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
public class FormEmailSendHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FormApplicationStatus formApplicationStatus;

    private String emailContent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Form form;

    @Builder
    public FormEmailSendHistory(FormApplicationStatus formApplicationStatus, String emailContent, Form form) {
        this.formApplicationStatus = formApplicationStatus;
        this.emailContent = emailContent;
        this.form = form;
    }
}
