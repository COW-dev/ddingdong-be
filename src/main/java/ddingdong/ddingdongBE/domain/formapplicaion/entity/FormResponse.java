package ddingdong.ddingdongBE.domain.formapplicaion.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FormResponse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String studentNumber;

    @Column(nullable = false)
    private String department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormResponseStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Form form;

    @Builder
    private FormResponse(String name, String studentNumber, String department, FormResponseStatus status, Form form) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.department = department;
        this.status = status;
        this.form = form;
    }
}
