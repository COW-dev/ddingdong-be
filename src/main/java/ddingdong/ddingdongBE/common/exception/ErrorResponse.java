package ddingdong.ddingdongBE.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;

@Schema(
        name = "ErrorResponse",
        description = "에러 응답"
)
@Builder
public record ErrorResponse(
        @Schema(description = "상태 코드", example = "400")
        int status,
        @Schema(description = "에러 메시지", example = "에러 메시지")
        String message,
        @Schema(description = "에러 시각", example = "2024-08-22T00:08:46.990585")
        LocalDateTime timestamp
) {

}
