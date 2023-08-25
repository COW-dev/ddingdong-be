package ddingdong.ddingdongBE.domain.club.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubMemberRequest;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
