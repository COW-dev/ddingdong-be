package ddingdong.ddingdongBE.domain.club.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@SQLDelete(sql = "update club set deleted_at = CURRENT_TIMESTAMP where id=?")
@Where(clause = "deleted_at IS NULL")
@Table(appliesTo = "club")
public class Club extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "club")
    private List<ClubMember> clubMembers = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String tag;

    @Column(nullable = false)
    private String leader;

    @Embedded
    @Column(nullable = false)
    private PhoneNumber phoneNumber;

    @Embedded
    @Column(nullable = false)
    private Location location;

    private LocalDateTime startRecruitPeriod;

    private LocalDateTime endRecruitPeriod;

    @Column(nullable = false)
    private String regularMeeting;

    @Column(nullable = false)
    private String introduction;

    @Column(nullable = false)
    private String activity;

    private String ideal;

    private String formUrl;

    @Embedded
    private Score score;

    private String profileImageUrl;

    private String introductionImageUrl;


    @Builder
    private Club(Long id, LocalDateTime deletedAt, User user, List<ClubMember> clubMembers, String name,
                 String category, String tag, String leader, PhoneNumber phoneNumber, Location location,
                 LocalDateTime startRecruitPeriod, LocalDateTime endRecruitPeriod, String regularMeeting,
                 String introduction, String activity, String ideal, String formUrl, Score score,
                 String profileImageUrl, String introductionImageUrl) {
        this.id = id;
        this.deletedAt = deletedAt;
        this.user = user;
        this.clubMembers = clubMembers;
        this.name = name;
        this.category = category;
        this.tag = tag;
        this.leader = leader;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.startRecruitPeriod = startRecruitPeriod;
        this.endRecruitPeriod = endRecruitPeriod;
        this.regularMeeting = regularMeeting;
        this.introduction = introduction;
        this.activity = activity;
        this.ideal = ideal;
        this.formUrl = formUrl;
        this.score = score;
        this.profileImageUrl = profileImageUrl;
        this.introductionImageUrl = introductionImageUrl;
    }

    public void updateClubInfo(Club updatedClub) {
        this.name = updatedClub.name;
        this.category = updatedClub.category;
        this.tag = updatedClub.tag;
        this.leader = updatedClub.leader;
        this.phoneNumber = updatedClub.phoneNumber;
        this.location = updatedClub.location;
        this.startRecruitPeriod = updatedClub.startRecruitPeriod;
        this.endRecruitPeriod = updatedClub.endRecruitPeriod;
        this.regularMeeting = updatedClub.regularMeeting;
        this.introduction = updatedClub.introduction;
        this.activity = updatedClub.activity;
        this.ideal = updatedClub.ideal;
        this.formUrl = updatedClub.formUrl;
        this.profileImageUrl = updatedClub.profileImageUrl;
        this.introductionImageUrl = updatedClub.introductionImageUrl;
    }

    public float editScore(Score score) {
        this.score = score;
        return this.score.getValue();
    }
}
