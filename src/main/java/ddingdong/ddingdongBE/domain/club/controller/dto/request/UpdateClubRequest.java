package ddingdong.ddingdongBE.domain.club.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.club.service.dto.UpdateClubCommand;
import ddingdong.ddingdongBE.file.controller.dto.request.FileMetaDataRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;

@Schema(
        name = "UpdateClubRequest",
        description = "동아리 - 동아리 수정 API 요청"
)
@Builder
public record UpdateClubRequest(
        @Schema(description = "동아리명", example = "cow")
        @NotNull(message = "동아리명은 필수입니다.")
        String name,
        @Schema(description = "분과", example = "학술")
        @NotNull(message = "분과는 필수입니다.")
        String category,
        @Schema(description = "태그", example = "IT")
        @NotNull(message = "태그는 필수입니다.")
        String tag,
        @Schema(description = "동아리 회장", example = "홍길동")
        @NotNull(message = "동아리 회장은 필수입니다.")
        String clubLeader,
        @Schema(description = "회장 전화번호", example = "010-1234-5678")
        @NotNull(message = "전화번호는 필수입니다.")
        String phoneNumber,
        @Schema(description = "동아리방 위치", example = "S0000")
        @NotNull(message = "동아리방 위치는 필수입니다.")
        String location,
        @Schema(description = "모집 시작 기간", example = "2024-08-19T00:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startRecruitPeriod,
        @Schema(description = "모집 마감 기간", example = "2024-08-19T00:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endRecruitPeriod,
        @Schema(description = "정기모임", example = "매주 월요일 18:00시")
        @NotNull(message = "정기모임 정보는 필수입니다.")
        String regularMeeting,
        @Schema(description = "동아리 소개", example = "소개")
        @NotNull(message = "동아리 소개는 필수입니다.")
        String introduction,
        @Schema(description = "활동 내용", example = "활동 내용")
        @NotNull(message = "활동 내용은 필수입니다.")
        String activity,
        @Schema(description = "인재상", example = "인재상")
        String ideal,
        @Schema(description = "모집 폼지", example = "https://form.com")
        String formUrl,
        FileMetaDataRequest profileImageFileInfo,
        FileMetaDataRequest introductionImageFileInfo
) {

    public UpdateClubCommand toCommand() {
        return UpdateClubCommand.builder()
                .name(name)
                .category(category)
                .tag(tag)
                .clubLeader(clubLeader)
                .phoneNumber(phoneNumber)
                .location(location)
                .startRecruitPeriod(startRecruitPeriod)
                .endRecruitPeriod(endRecruitPeriod)
                .regularMeeting(regularMeeting)
                .introduction(introduction)
                .activity(activity)
                .ideal(ideal)
                .formUrl(formUrl)
                .profileImageFileMetaDataCommand(
                        Optional.ofNullable(profileImageFileInfo).map(FileMetaDataRequest::toCommand).orElse(null)
                )
                .introductionImageFileMetaDataCommand(
                        Optional.ofNullable(introductionImageFileInfo).map(FileMetaDataRequest::toCommand).orElse(null)
                )
                .build();
    }
}
