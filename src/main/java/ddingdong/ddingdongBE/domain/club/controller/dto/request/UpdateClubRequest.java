package ddingdong.ddingdongBE.domain.club.controller.dto.request;

import java.util.List;
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

    private String startRecruitPeriod;

    private String endRecruitPeriod;

    private String regularMeeting;

    private String introduction;

    private String activity;

    private String ideal;

    private String formUrl;

    private List<String> profileImageUrls;

    private List<String> introduceImageUrls;

}
