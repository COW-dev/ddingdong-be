package ddingdong.ddingdongBE.domain.club.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update club set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class Club extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "club")
    private List<ClubMember> clubMembers = new ArrayList<>();

    private String name;

    private String category;

    private String tag;

    private String leader;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private Location location;

    private LocalDateTime startRecruitPeriod;

    private LocalDateTime endRecruitPeriod;

    private String regularMeeting;

    private String introduction;

    private String activity;

    private String ideal;

    private String formUrl;

    private String profileImageKey;

    private String introductionImageKey;

    @Embedded
    private Score score;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    private Club(Long id, User user, List<ClubMember> clubMembers, String name, String category, String tag,
                 String leader, PhoneNumber phoneNumber, Location location, LocalDateTime startRecruitPeriod,
                 LocalDateTime endRecruitPeriod, String regularMeeting, String introduction, String activity,
                 String ideal, String formUrl, String profileImageKey, String introductionImageKey, Score score,
                 LocalDateTime deletedAt) {
        this.id = id;
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
        this.profileImageKey = profileImageKey;
        this.introductionImageKey = introductionImageKey;
        this.score = score;
        this.deletedAt = deletedAt;
    }

    public void updateClubInfo(Club club) {
        this.name = club.getName();
        this.category = club.getCategory();
        this.tag = club.getTag();
        this.leader = club.getLeader();
        this.phoneNumber = club.getPhoneNumber();
        this.location = club.getLocation();
        this.startRecruitPeriod = club.getStartRecruitPeriod();
        this.endRecruitPeriod = club.getEndRecruitPeriod();
        this.regularMeeting = club.getRegularMeeting();
        this.introduction = club.getIntroduction();
        this.activity = club.getActivity();
        this.ideal = club.getIdeal();
        this.formUrl = club.getFormUrl();
        this.profileImageKey = club.getProfileImageKey();
        this.introductionImageKey = club.getIntroductionImageKey();
    }

    public BigDecimal editScore(Score score) {
        this.score = score;
        return this.score.getValue();
    }
}
