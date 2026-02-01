package ddingdong.ddingdongBE.domain.pairgame.controller.dto.response;

import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameApplierAmountQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record PairGameApplierAmountResponse(
        @Schema(description = "총 응모자 수", example = "200")
        int amount
) {
   public static PairGameApplierAmountResponse from(PairGameApplierAmountQuery query) {
       return PairGameApplierAmountResponse.builder()
               .amount(query.amount())
               .build();
   }
}
