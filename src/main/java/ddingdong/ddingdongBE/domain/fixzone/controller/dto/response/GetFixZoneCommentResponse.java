package ddingdong.ddingdongBE.domain.fixzone.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "GetFixZoneCommentResponse", description = "Fix Zone Comment 응답")
public record GetFixZoneCommentResponse(
    @Schema(description = "댓글 id")
    Long commentId,

    @Schema(description = "댓글 작성자")
    String commentor,

    @Schema(description = "댓글 내용")
    String content,

    @Schema(description = "작성자 프로필 이미지 URL")
    String profileImageUrl,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "댓글 작성 시각", example = "2024-07-30 22:30:00")
    LocalDateTime createdAt
) {

    public static GetFixZoneCommentResponse from(
        FixZoneComment comment,
        String profileImageUrl
    ) {
        return new GetFixZoneCommentResponse(
            comment.getId(),
            comment.getClub().getName(),
            comment.getContent(),
            profileImageUrl,
            comment.getCreatedAt()
        );
    }

}

