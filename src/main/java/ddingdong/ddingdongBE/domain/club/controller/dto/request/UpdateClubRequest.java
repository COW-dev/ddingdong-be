package ddingdong.ddingdongBE.domain.club.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startRecruitPeriod;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endRecruitPeriod;

    private String regularMeeting;

    private String introduction;

    private String activity;

    private String ideal;

    private String formUrl;

    private List<String> profileImageUrls;

    private List<String> introduceImageUrls;

}
