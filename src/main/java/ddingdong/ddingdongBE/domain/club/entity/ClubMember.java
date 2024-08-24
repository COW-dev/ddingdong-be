package ddingdong.ddingdongBE.domain.club.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update club_member set deleted_at = CURRENT_TIMESTAMP where id=?")
@Where(clause = "deleted_at IS NULL")
@Table(appliesTo = "club_member")
public class ClubMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    private String name;

    private String studentNumber;

    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private Position position;

    private String department;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public ClubMember(Long id, Club club, String name, String studentNumber, String phoneNumber, Position position,
                      String department) {
        this.id = id;
        this.club = club;
        this.name = name;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.department = department;
    }

    public void updateInformation(ClubMember updateClubMember) {
        this.name = updateClubMember.getName();
        this.studentNumber = updateClubMember.getStudentNumber();
        this.phoneNumber = updateClubMember.getPhoneNumber();
        this.position = updateClubMember.getPosition();
        this.department = updateClubMember.getDepartment();
    }
}
