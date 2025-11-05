package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CurrentTermResponse(
    @Schema(description = "현재 활동 회차", example = "1")
    String term
) {

    public static CurrentTermResponse from(String term) {
        return CurrentTermResponse.builder()
            .term(term)
            .build();
    }
}
