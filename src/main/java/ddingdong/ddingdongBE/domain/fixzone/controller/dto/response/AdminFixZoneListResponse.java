package ddingdong.ddingdongBE.domain.fixzone.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.AdminFixZoneListQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "ClubGetFixZoneResponse", description = "어드민 - 픽스존 목록 조회 응답")
public record AdminFixZoneListResponse(

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
    @Schema(description = "요청 시각", example = "2023-07-23 14:55:00")
    LocalDateTime requestedAt
) {

    public static AdminFixZoneListResponse from(AdminFixZoneListQuery query) {
        return new AdminFixZoneListResponse(
                query.fixZoneId(),
                query.clubLocation(),
                query.clubName(),
                query.title(),
                query.isCompleted(),
                query.requestedAt()
        );
    }

}
