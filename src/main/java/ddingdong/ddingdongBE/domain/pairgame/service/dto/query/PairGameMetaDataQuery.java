package ddingdong.ddingdongBE.domain.pairgame.service.dto.query;

import lombok.Builder;

import java.util.List;

@Builder
public record PairGameMetaDataQuery(
        List<String> images
) {
    public static PairGameMetaDataQuery of(List<String> images) {
        return PairGameMetaDataQuery.builder()
                .images(images)
                .build();
    }
}
