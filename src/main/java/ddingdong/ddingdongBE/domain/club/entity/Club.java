package ddingdong.ddingdongBE.domain.club.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubRequest;
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
import java.time.format.DateTimeFormatter;
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

    private String content;

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

    @Embedded
    private Score score;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Club(Long id, User user, String name, String category, String tag, String leader, Location location,
                PhoneNumber phoneNumber, Score score) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.category = category;
        this.tag = tag;
        this.leader = leader;
        this.location = location;
        this.score = score;
        this.phoneNumber = phoneNumber;
    }

    public void updateClubInfo(UpdateClubRequest request) {
        this.name = request.getName() != null ? request.getName() : this.name;
        this.category = request.getCategory() != null ? request.getCategory() : this.category;
        this.tag = request.getTag() != null ? request.getTag() : this.tag;
        this.content = request.getContent() != null ? request.getContent() : this.content;
        this.leader = request.getClubLeader() != null ? request.getClubLeader() : this.leader;
        this.phoneNumber =
                request.getPhoneNumber() != null ? PhoneNumber.from(request.getPhoneNumber()) : this.phoneNumber;
        this.location = request.getLocation() != null ? Location.from(request.getLocation()) : this.location;
        this.startRecruitPeriod =
                request.getStartRecruitPeriod().isBlank() ? null : parseLocalDateTime(request.getStartRecruitPeriod());
        this.endRecruitPeriod =
                request.getEndRecruitPeriod().isBlank() ? null : parseLocalDateTime(request.getEndRecruitPeriod());
        this.regularMeeting = request.getRegularMeeting() != null ? request.getRegularMeeting() : this.regularMeeting;
        this.introduction = request.getIntroduction() != null ? request.getIntroduction() : this.introduction;
        this.activity = request.getActivity() != null ? request.getActivity() : this.activity;
        this.ideal = request.getIdeal() != null ? request.getIdeal() : this.ideal;
        this.formUrl = request.getFormUrl() != null ? request.getFormUrl() : this.formUrl;
    }

    public BigDecimal editScore(Score score) {
        this.score = score;
        return this.score.getValue();
    }

    private LocalDateTime parseLocalDateTime(String inputLocalDateTimeFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(inputLocalDateTimeFormat, formatter);
    }
}
