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
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"studentName", "studentNumber"}))
@TypeDef(name = "json", typeClass = JsonType.class)
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

    @Builder
    private StampHistory(Long id, String studentName, String studentNumber, LocalDateTime completedAt) {
        this.id = id;
        this.studentName = studentName;
        this.studentNumber = studentNumber;
        this.completedAt = completedAt;
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

}
