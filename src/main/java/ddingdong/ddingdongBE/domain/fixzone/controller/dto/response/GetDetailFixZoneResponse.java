package ddingdong.ddingdongBE.domain.fixzone.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "상세 FixZone 응답")
public record GetDetailFixZoneResponse(

    @Schema(description = "픽스존 id")
    Long id,

    @Schema(description = "제목")
    String title,

    @Schema(description = "내용")
    String content,

    @Schema(description = "픽스존 완료 처리 여부")
    boolean isCompleted,

    @Schema(description = "요청 시각", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime requestedAt,

    @Schema(description = "이미지 URL 목록")
    List<String> imageUrls,

    @Schema(description = "Fix Zone 댓글 목록")
    List<GetFixZoneCommentResponse> comments
) {

    public static GetDetailFixZoneResponse of(
        Long id,
        String title,
        LocalDateTime createdAt,
        String content,
        boolean isCompleted,
        List<String> imageUrls,
        List<GetFixZoneCommentResponse> comments
    ) {
        return new GetDetailFixZoneResponse(
            id,
            title,
            content,
            isCompleted,
            createdAt,
            imageUrls,
            comments
        );
    }

}
