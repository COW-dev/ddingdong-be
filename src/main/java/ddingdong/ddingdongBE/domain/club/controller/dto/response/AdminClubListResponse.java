package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import ddingdong.ddingdongBE.domain.club.service.dto.query.AdminClubListQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedImageUrlQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

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
        AdminClubListImageUrlResponse profileImageUrl
) {

    public static AdminClubListResponse from(AdminClubListQuery query) {
        return new AdminClubListResponse(
                query.id(),
                query.name(),
                query.category(),
                query.score(),
                AdminClubListImageUrlResponse.from(query.profileImageUrlQuery())
        );
    }

    @Schema(
            name = "AdminClubListImageUrlResponse",
            description = "어드민 - 동아리 목록 이미지 URL 조회 응답"
    )
    record AdminClubListImageUrlResponse(
            @Schema(description = "원본 url", example = "url")
            String originUrl,
            @Schema(description = "cdn url", example = "url")
            String cdnUrl
    ) {

        public static AdminClubListImageUrlResponse from(UploadedImageUrlQuery query) {
            return new AdminClubListImageUrlResponse(query.originUrl(), query.cdnUrl());
        }

    }

}
