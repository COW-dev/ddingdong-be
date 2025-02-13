package ddingdong.ddingdongBE.domain.formapplication.service.dto.query;

import lombok.Builder;

@Builder
public record FormApplicationCountQuery(
        int formApplicationCount
) {
    public static FormApplicationCountQuery of(int formApplicationCount) {
        return FormApplicationCountQuery.builder()
                .formApplicationCount(formApplicationCount)
                .build();
    }
}
