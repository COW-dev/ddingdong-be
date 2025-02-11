package ddingdong.ddingdongBE.domain.club.service.dto.command;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.entity.PhoneNumber;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record UpdateClubInfoCommand(
        Long userId,
        String name,
        String category,
        String tag,
        String clubLeader,
        String phoneNumber,
        String location,
        String startRecruitPeriod,
        String endRecruitPeriod,
        String regularMeeting,
        String introduction,
        String activity,
        String ideal,
        Long formId,
        String profileImageId,
        String introductionImageId
) {

    public Club toEntity() {
        return Club.builder()
                .name(name)
                .category(category)
                .tag(tag)
                .leader(clubLeader)
                .phoneNumber(PhoneNumber.from(phoneNumber))
                .location(Location.from(location))
                .startRecruitPeriod(parseLocalDateTime(startRecruitPeriod))
                .endRecruitPeriod(parseLocalDateTime(endRecruitPeriod))
                .regularMeeting(regularMeeting)
                .introduction(introduction)
                .activity(activity)
                .ideal(ideal)
                .formId(formId)
                .build();
    }

    private LocalDateTime parseLocalDateTime(String inputLocalDateTimeFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(inputLocalDateTimeFormat, formatter);
    }
}
