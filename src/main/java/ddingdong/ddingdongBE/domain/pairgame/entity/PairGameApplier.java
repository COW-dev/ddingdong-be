package ddingdong.ddingdongBE.domain.pairgame.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "update pair_game_applier set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class PairGameApplier extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String department;

    @Column(unique = true, nullable = false, length = 50)
    private String studentNumber;

    @Column(nullable = false, length = 50)
    private String phoneNumber;

    @Column(nullable = false)
    private String studentFeeImageKey;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    private PairGameApplier(String name, String department, String studentNumber, String phoneNumber, String studentFeeImageKey) {
        this.name = name;
        this.department = department;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.studentFeeImageKey = studentFeeImageKey;
    }

}
