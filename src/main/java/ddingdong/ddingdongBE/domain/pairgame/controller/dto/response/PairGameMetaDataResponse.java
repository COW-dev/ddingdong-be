package ddingdong.ddingdongBE.domain.pairgame.controller.dto.response;

import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameMetaDataQuery;
import lombok.Builder;

import java.util.List;

@Builder
public record PairGameMetaDataResponse(
    List<String> images
) {
    public static PairGameMetaDataResponse from(PairGameMetaDataQuery query) {
        return PairGameMetaDataResponse.builder()
                .images(query.images())
                .build();
    }
}
