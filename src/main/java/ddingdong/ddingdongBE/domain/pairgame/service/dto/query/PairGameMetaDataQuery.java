package ddingdong.ddingdongBE.domain.pairgame.service.dto.query;

import lombok.Builder;

import java.util.List;

@Builder
public record PairGameMetaDataQuery(
        List<PairGameClubAndImageQuery> metaData
) {
    @Builder
    public record PairGameClubAndImageQuery(
            String clubName,
            String category,
            String imageUrl
    ) {
        public static PairGameClubAndImageQuery of(String clubName, String category, String imageUrl) {
            return PairGameClubAndImageQuery.builder()
                    .clubName(clubName)
                    .category(category)
                    .imageUrl(imageUrl)
                    .build();
        }
    }
    public static PairGameMetaDataQuery of(List<PairGameClubAndImageQuery> metaData) {
        return PairGameMetaDataQuery.builder()
                .metaData(metaData)
                .build();
    }
}
