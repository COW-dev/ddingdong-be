package ddingdong.ddingdongBE.domain.fixzone.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "ClubGetFixZoneResponse", description = "Club - 픽스존 조회 응답")
public record GetFixZoneResponse(

    @Schema(description = "Fix zone ID")
    Long fixZoneId,

    @Schema(description = "동아리방 위치")
    String clubLocation,

    @Schema(description = "클럽명")
    String clubName,

    @Schema(description = "제목")
    String title,

    @Schema(description = "처리 완료 여부")
    boolean isCompleted,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "요청 시각", example = "2023-07-23 14:55:00")
    LocalDateTime requestedAt
) {

    public static GetFixZoneResponse from(FixZone fixZone) {
        return new GetFixZoneResponse(
            fixZone.getId(),
            fixZone.getClub().getLocation().getValue(),
            fixZone.getClub().getName(),
            fixZone.getTitle(),
            fixZone.isCompleted(),
            fixZone.getCreatedAt()
        );
    }

}

