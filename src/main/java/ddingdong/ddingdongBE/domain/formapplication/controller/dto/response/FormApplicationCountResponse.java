package ddingdong.ddingdongBE.domain.formapplication.controller.dto.response;

import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationCountQuery;
import lombok.Builder;

@Builder
public record FormApplicationCountResponse(
        int formApplicationCount
) {

    public static FormApplicationCountResponse from(FormApplicationCountQuery query) {
        return FormApplicationCountResponse.builder()
                .formApplicationCount(query.formApplicationCount())
                .build();
    }
}
