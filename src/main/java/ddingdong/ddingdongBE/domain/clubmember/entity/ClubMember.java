package ddingdong.ddingdongBE.domain.clubmember.entity;

import static ddingdong.ddingdongBE.domain.club.entity.Position.MEMBER;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Position;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update club_member set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
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

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
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

    public static ClubMember createFromFormApplication(FormApplication formApplication) {
        return ClubMember.builder()
                .name(formApplication.getName())
                .studentNumber(formApplication.getStudentNumber())
                .department(formApplication.getDepartment())
                .phoneNumber(formApplication.getPhoneNumber())
                .position(MEMBER)
                .build();
    }

    public void updateInformation(ClubMember updateClubMember) {
        this.name = updateClubMember.getName();
        this.studentNumber = updateClubMember.getStudentNumber();
        this.phoneNumber = updateClubMember.getPhoneNumber();
        this.position = updateClubMember.getPosition();
        this.department = updateClubMember.getDepartment();
    }

    public void setClubForConvenience(Club club) {
        this.club = club;
    }

    public void validateBelongsToClub(@NonNull final Club targetClub) {
        if (this.club == null || !Objects.equals(this.club.getId(), targetClub.getId())) {
            throw new IllegalArgumentException("동아리원은 해당 동아리에 속해 있지 않습니다");
        }
    }
}
