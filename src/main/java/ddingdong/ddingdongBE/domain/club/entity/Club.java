package ddingdong.ddingdongBE.domain.club.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubRequest;
import ddingdong.ddingdongBE.domain.user.entity.User;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String category;

    private String tag;

    private String content;

    private String leader;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private Location location;

    private String recruitPeriod;

    private String regularMeeting;

    private String isRecruit;

    private String introduction;

    private String activity;

    private String ideal;

    private String formUrl;

    @Embedded
    private Score score;

    @Builder
    public Club(User user, String name, String category, String tag, String leader, Location location,
                Score score) {
        this.user = user;
        this.name = name;
        this.category = category;
        this.tag = tag;
        this.leader = leader;
        this.location = location;
        this.score = score;
    }

    public void updateClubInfo(UpdateClubRequest request) {
        this.name = request.getName() != null ? request.getName() : this.name;
        this.category = request.getCategory() != null ? request.getCategory() : this.category;
        this.tag = request.getTag() != null ? request.getTag() : this.tag;
        this.content = request.getContent() != null ? request.getContent() : this.content;
        this.leader = request.getClubLeader() != null ? request.getClubLeader() : this.leader;
        this.phoneNumber = request.getPhoneNumber() != null ? PhoneNumber.of(request.getPhoneNumber()) : this.phoneNumber;
        this.location = request.getLocation() != null ? Location.of(request.getLocation()) : this.location;
        this.recruitPeriod = request.getRecruitPeriod() != null ? request.getRecruitPeriod() : this.recruitPeriod;
        this.regularMeeting = request.getRegularMeeting() != null ? request.getRegularMeeting() : this.regularMeeting;
        this.isRecruit = request.getIsRecruit() != null ? request.getIsRecruit() : this.isRecruit;
        this.introduction = request.getIntroduction() != null ? request.getIntroduction() : this.introduction;
        this.activity = request.getActivity() != null ? request.getActivity() : this.activity;
        this.ideal = request.getIdeal() != null ? request.getIdeal() : this.ideal;
        this.formUrl = request.getFormUrl() != null ? request.getFormUrl() : this.formUrl;
    }

    public void editScore(int score) {
        this.score = Score.of(score);
    }

}
