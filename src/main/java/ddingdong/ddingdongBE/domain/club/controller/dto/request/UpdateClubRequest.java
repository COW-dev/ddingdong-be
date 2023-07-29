package ddingdong.ddingdongBE.domain.club.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateClubRequest {

    private String name;

    private String category;

    private String tag;

    private String content;

    private String clubLeader;

    private String phoneNumber;

    private String location;

    private String recruitPeriod;

    private String regularMeeting;

    private String isRecruit;

    private String introduction;

    private String activity;

    private String ideal;

    private String formUrl;

}
