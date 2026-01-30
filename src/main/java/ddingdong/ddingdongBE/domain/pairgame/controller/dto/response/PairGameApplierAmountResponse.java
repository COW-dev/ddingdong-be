package ddingdong.ddingdongBE.domain.pairgame.controller.dto.response;

import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameApplierAmountQuery;
import lombok.Builder;

@Builder
public record PairGameApplierAmountResponse(
        int amount
) {
   public static PairGameApplierAmountResponse from(PairGameApplierAmountQuery query) {
       return PairGameApplierAmountResponse.builder()
               .amount(query.amount())
               .build();
   }
}
