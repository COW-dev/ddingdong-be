package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import ddingdong.ddingdongBE.domain.club.service.dto.query.AdminClubListQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

@Schema(
        name = "AdminClubListResponse",
        description = "어드민 - 동아리 목록 조회 응답"
)
public record AdminClubListResponse(
        @Schema(description = "동아리 식별자", example = "1")
        Long id,
        @Schema(description = "동아리명", example = "동아리명")
        String name,
        @Schema(description = "카테고리", example = "사회연구")
        String category,
        @Schema(description = "동아리 점수", example = "0.00")
        BigDecimal score,
        @Schema(description = "동아리 프로필 이미지 Url", example = "url")
        List<String> profileImageUrls
) {

    public static AdminClubListResponse from(AdminClubListQuery query) {
        return new AdminClubListResponse(
                query.id(),
                query.name(),
                query.category(),
                query.score(),
                query.profileImageUrls()
        );
    }

}
