package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.entity.PhoneNumber;
import java.time.LocalDateTime;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startRecruitPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endRecruitPeriod;

    private String regularMeeting;

    private String introduction;

    private String activity;

    private String ideal;

    private String formUrl;

    private List<String> profileImageUrls;

    private List<String> introduceImageUrls;


    @Builder
    public DetailClubResponse(String name, String category, String tag, String content, String leader,
                              PhoneNumber phoneNumber, Location location, LocalDateTime startRecruitPeriod,
                              LocalDateTime endRecruitPeriod, String regularMeeting, String introduction,
                              String activity, String ideal, String formUrl,
                              List<String> profileImageUrls, List<String> introduceImageUrls) {
        this.name = name;
        this.category = category;
        this.tag = tag;
        this.content = content;
        this.leader = leader;
        this.phoneNumber = phoneNumber.getNumber();
        this.location = location.getValue();
        this.startRecruitPeriod = startRecruitPeriod;
        this.endRecruitPeriod = endRecruitPeriod;
        this.regularMeeting = regularMeeting;
        this.introduction = introduction;
        this.activity = activity;
        this.ideal = ideal;
        this.formUrl = formUrl;
        this.profileImageUrls = profileImageUrls;
        this.introduceImageUrls = introduceImageUrls;
    }

    public static DetailClubResponse of(Club club, List<String> profileImageUrls, List<String> introduceImageUrls) {
        return DetailClubResponse.builder()
                .name(club.getName())
                .category(club.getCategory())
                .tag(club.getTag())
                .content(club.getContent())
                .leader(club.getLeader())
                .phoneNumber(club.getPhoneNumber())
                .location(club.getLocation())
                .startRecruitPeriod(club.getStartRecruitPeriod())
                .endRecruitPeriod(club.getEndRecruitPeriod())
                .regularMeeting(club.getRegularMeeting())
                .introduction(club.getIntroduction())
                .activity(club.getActivity())
                .ideal(club.getIdeal())
                .formUrl(club.getFormUrl())
                .profileImageUrls(profileImageUrls)
                .introduceImageUrls(introduceImageUrls).build();
    }

}
