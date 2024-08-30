package ddingdong.ddingdongBE.domain.club.service.dto;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.entity.PhoneNumber;
import ddingdong.ddingdongBE.file.service.dto.FileMetaDataCommand;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record UpdateClubCommand(
        String name,
        String category,
        String tag,
        String clubLeader,
        String phoneNumber,
        String location,
        LocalDateTime startRecruitPeriod,
        LocalDateTime endRecruitPeriod,
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
                .startRecruitPeriod(startRecruitPeriod)
                .endRecruitPeriod(endRecruitPeriod)
                .regularMeeting(regularMeeting)
                .introduction(introduction)
                .activity(activity)
                .ideal(ideal)
                .formUrl(formUrl)
                .profileImageUrl(profileImageUrl)
                .introductionImageUrl(introductionImageUrl)
                .build();
    }

}
