package ddingdong.ddingdongBE.domain.fixzone.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralMyFixZoneListQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "ClubGetFixZoneResponse", description = "Club - 픽스존 조회 응답")
public record CentralMyFixZoneListResponse(

    @Schema(description = "Fix zone ID")
    Long id,
    @Schema(description = "동아리방 위치")
    String clubLocation,
    @Schema(description = "클럽명")
    String clubName,
    @Schema(description = "제목")
    String title,
    @Schema(description = "처리 완료 여부")
    boolean isCompleted,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "요청 시각", pattern = "yyyy-MM-dd HH:mm:ss", example = "2023-07-23 14:55:00")
    LocalDateTime requestedAt
) {

    public static CentralMyFixZoneListResponse from(CentralMyFixZoneListQuery query) {
        return new CentralMyFixZoneListResponse(
                query.id(),
                query.clubLocation(),
                query.clubName(),
                query.title(),
                query.isCompleted(),
                query.requestedAt()
        );
    }

}
