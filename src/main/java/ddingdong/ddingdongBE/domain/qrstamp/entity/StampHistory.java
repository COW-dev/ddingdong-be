package ddingdong.ddingdongBE.domain.qrstamp.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
@SQLDelete(sql = "update stamp_history set deleted_at = CURRENT_TIMESTAMP where id=?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "stamp_history", uniqueConstraints = @UniqueConstraint(columnNames = {"studentName", "studentNumber"}))
public class StampHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String studentNumber;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private final Map<ClubStamp, LocalDateTime> collectedStamps = new HashMap<>();

    private LocalDateTime completedAt;

    private String department;

    private String telephone;

    private String certificationImageUrl;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    private StampHistory(Long id, String studentName, String department, String studentNumber,
                         String telephone, LocalDateTime completedAt, String certificationImageUrl) {
        this.id = id;
        this.studentName = studentName;
        this.department = department;
        this.studentNumber = studentNumber;
        this.telephone = telephone;
        this.completedAt = completedAt;
        this.certificationImageUrl = certificationImageUrl;
    }

    public void collectStamp(ClubStamp clubStamp, LocalDateTime collectedAt) {
        this.collectedStamps.put(clubStamp, collectedAt);
        if (this.collectedStamps.size() == 10) {
            this.completedAt = LocalDateTime.now();
        }
    }

    public boolean isCompleted() {
        return this.collectedStamps.size() >= 10;
    }


    public void apply(String telephone, String certificationImageUrl) {
        this.telephone = telephone;
        this.certificationImageUrl = certificationImageUrl;
    }

}
