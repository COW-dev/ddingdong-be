package ddingdong.ddingdongBE.domain.pairgame.controller.dto.response;

import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameMetaDataQuery.PairGameClubAndImageQuery;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameMetaDataQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record PairGameMetaDataResponse(
        @ArraySchema(schema = @Schema(implementation = PairGameMetaDataResponse.PairGameClubAndImageResponse.class))
        List<PairGameClubAndImageResponse> metaData
) {
    @Builder
    public record PairGameClubAndImageResponse(
            @Schema(description = "동아리 이름", example = "COW")
            String clubName,
            @Schema(description = "동아리 분과", example = "사회연구")
            String category,
            @Schema(description = "동아리 로고 이미지 CDN URL", example = "https://cdn.com")
            String imageUrl
    ) {
        public static PairGameClubAndImageResponse from(PairGameClubAndImageQuery query) {
            return PairGameClubAndImageResponse.builder()
                    .clubName(query.clubName())
                    .category(query.category())
                    .imageUrl(query.imageUrl())
                    .build();
        }
    }
    public static PairGameMetaDataResponse from(PairGameMetaDataQuery query) {
        List<PairGameClubAndImageResponse> responses = query.metaData().stream()
                .map(PairGameClubAndImageResponse::from)
                .toList();

        return PairGameMetaDataResponse.builder()
                .metaData(responses)
                .build();
    }
}
