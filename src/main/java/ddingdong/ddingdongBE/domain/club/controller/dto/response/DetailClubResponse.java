package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.entity.PhoneNumber;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DetailClubResponse {

    private String name;

    private String category;

    private String tag;

    private String content;

    private String leader;

    private String phoneNumber;

    private String location;

    private String recruitPeriod;

    private String regularMeeting;

    private String isRecruit;

    private String introduction;

    private String activity;

    private String ideal;

    private String formUrl;

    private List<String> imageUrls;


    @Builder
    public DetailClubResponse(String name, String category, String tag, String content, String leader, String isRecruit,
                              PhoneNumber phoneNumber, Location location, String recruitPeriod, String regularMeeting,
                              String introduction, String activity, String ideal, String formUrl,
                              List<String> imageUrls) {
        this.name = name;
        this.category = category;
        this.tag = tag;
        this.content = content;
        this.leader = leader;
        this.phoneNumber = phoneNumber.getNumber();
        this.location = location.getValue();
        this.recruitPeriod = recruitPeriod;
        this.regularMeeting = regularMeeting;
        this.introduction = introduction;
        this.activity = activity;
        this.ideal = ideal;
        this.formUrl = formUrl;
        this.isRecruit = isRecruit;
        this.imageUrls = imageUrls;
    }

    public static DetailClubResponse of(Club club, List<String> imageUrls) {
        return DetailClubResponse.builder()
                .name(club.getName())
                .category(club.getCategory())
                .tag(club.getTag())
                .content(club.getContent())
                .leader(club.getLeader())
                .phoneNumber(club.getPhoneNumber())
                .location(club.getLocation())
                .recruitPeriod(club.getRecruitPeriod())
                .regularMeeting(club.getRegularMeeting())
                .introduction(club.getIntroduction())
                .activity(club.getActivity())
                .ideal(club.getIdeal())
                .formUrl(club.getFormUrl())
                .isRecruit(club.getIsRecruit())
                .imageUrls(imageUrls).build();
    }

}
