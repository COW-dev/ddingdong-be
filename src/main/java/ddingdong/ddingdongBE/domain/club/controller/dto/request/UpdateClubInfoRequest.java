package ddingdong.ddingdongBE.domain.club.controller.dto.request;

import ddingdong.ddingdongBE.domain.club.service.dto.command.UpdateClubInfoCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(
        name = "UpdateClubInfoRequest",
        description = "중앙동아리 - 동아리 정보 수정 요청"
)
public record UpdateClubInfoRequest(
        @Schema(description = "동아리명", example = "cow")
        @NotNull(message = "동아리명은 필수로 입력해야 합니다.")
        String name,
        @Schema(description = "카테고리", example = "사회연구")
        @NotNull(message = "카테고리는 필수로 입력해야 합니다.")
        String category,
        @Schema(description = "분과", example = "IT")
        @NotNull(message = "분과는 필수로 입력해야 합니다.")
        String tag,
        @Schema(description = "동아리 회장", example = "홍길동")
        @NotNull(message = "동아리회장는 필수로 입력해야 합니다.")
        String clubLeader,
        @Schema(description = "연락처", example = "010-1234-5678")
        @NotNull(message = "연락처는 필수로 입력해야 합니다.")
        String phoneNumber,
        @Schema(description = "동아리방 위치", example = "S1111")
        @NotNull(message = "동아리방 위치는 필수로 입력해야 합니다.")
        String location,
        @Schema(description = "모집시작 날짜", example = "2024-01-01 00:00")
        String startRecruitPeriod,
        @Schema(description = "모집마감 날짜", example = "2024-01-01 00:00")
        String endRecruitPeriod,
        @Schema(description = "정기활동", example = "정기활동")
        @NotNull(message = "정기활동은 필수로 입력해야 합니다.")
        String regularMeeting,
        @Schema(description = "동아리 소개", example = "소개")
        @NotNull(message = "동아리 소개는 필수로 입력해야 합니다.")
        String introduction,
        @Schema(description = "동아리 활동", example = "활동")
        @NotNull(message = "동아리 활동은 필수로 입력해야 합니다.")
        String activity,
        @Schema(description = "인재상", example = "인재상")
        String ideal,
        @Schema(description = "모집 Url", example = "Url")
        String formUrl,
        @Schema(description = "동아리 프로필 이미지 key", example = "local/file/2024-01-01/abc/uuid")
        String profileImageKey,
        @Schema(description = "동아리 소개 이미지 key", example = "local/file/2024-01-01/abc/uuid")
        String introductionImageKey

) {

    public UpdateClubInfoCommand toCommand(Long userId) {
        return new UpdateClubInfoCommand(
                userId,
                name,
                category,
                tag,
                clubLeader,
                phoneNumber,
                location,
                startRecruitPeriod,
                endRecruitPeriod,
                regularMeeting,
                introduction,
                activity,
                ideal,
                formUrl,
                profileImageKey,
                introductionImageKey
        );
    }
}
