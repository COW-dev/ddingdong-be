package ddingdong.ddingdongBE.domain.club.service.dto;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.entity.PhoneNumber;
import ddingdong.ddingdongBE.file.service.dto.FileMetaDataCommand;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import lombok.Builder;

@Builder
public record UpdateClubCommand(
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
        String formUrl,
        FileMetaDataCommand profileImageFileMetaDataCommand,
        FileMetaDataCommand introductionImageFileMetaDataCommand
) {

    public Club toEntity(String profileImageUrl, String introductionImageUrl) {
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
                .formUrl(formUrl)
                .profileImageUrl(profileImageUrl)
                .introductionImageUrl(introductionImageUrl)
                .build();
    }

    private LocalDateTime parseLocalDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateTimeString, e);
        }
    }

}
