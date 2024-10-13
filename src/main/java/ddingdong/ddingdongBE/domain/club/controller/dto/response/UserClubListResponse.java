package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubListQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "UserClubListResponse",
        description = "유저 - 동아리 목록 조회 응답"
)
public record UserClubListResponse(
        @Schema(description = "동아리 식별자", example = "1")
        Long id,
        @Schema(description = "동아리명", example = "동아리명")
        String name,
        @Schema(description = "카테고리", example = "사회연구")
        String category,
        @Schema(description = "분과", example = "IT")
        String tag,
        @Schema(description = "모집 가능 여부", example = "모집 예정/모집 중/모집 마감")
        String recruitStatus
) {

    public static UserClubListResponse from(UserClubListQuery query) {
        return new UserClubListResponse(
                query.id(),
                query.name(),
                query.category(),
                query.tag(),
                query.recruitStatus()
        );
    }

}
