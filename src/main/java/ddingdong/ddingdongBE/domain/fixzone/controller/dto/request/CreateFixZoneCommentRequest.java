package ddingdong.ddingdongBE.domain.fixzone.controller.dto.request;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateFixZoneCommentRequest", description = "Admin - 픽스존 댓글 등록 요청")
public record CreateFixZoneCommentRequest(
    @Schema(description = "댓글 내용")
    String content
) {

    public FixZoneComment toEntity(FixZone fixZone, Club club) {
        return FixZoneComment.builder()
            .fixZone(fixZone)
            .club(club)
            .content(content)
            .build();
    }

}

