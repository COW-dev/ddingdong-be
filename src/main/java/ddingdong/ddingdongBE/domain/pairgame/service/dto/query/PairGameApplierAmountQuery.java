package ddingdong.ddingdongBE.domain.pairgame.service.dto.query;

import lombok.Builder;

@Builder
public record PairGameApplierAmountQuery(
        int amount
) {
    public static PairGameApplierAmountQuery of(int amount) {
        return PairGameApplierAmountQuery.builder()
                .amount(amount).build();
    }
}
